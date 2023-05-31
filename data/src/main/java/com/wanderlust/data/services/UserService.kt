package com.wanderlust.data.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.wanderlust.domain.model.FailRegister
import com.wanderlust.domain.model.RegisterResult
import com.wanderlust.domain.model.SuccessRegister
import com.wanderlust.domain.repositories.UserRepository
import com.wanderlust.domain.services.UserService
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class UserService @Inject constructor(private val auth: FirebaseAuth, private val repository: UserRepository) :
    UserService {
    val currentUser get() = auth.currentUser

    override suspend fun register(username: String, email: String, password: String): RegisterResult {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            if (authResult.user == null) throw Exception("Unknown error")
            val user = repository.createUser(authResult.user!!.uid, username)
            SuccessRegister(user)
        } catch (e: FirebaseAuthException) {
            FailRegister(e.message)
        } catch (e: FirebaseFirestoreException) {
            currentUser?.delete()?.await()
            FailRegister("Не удалось завершить регистрацию, попробуйте еще раз.")
        }
    }

    override suspend fun sendVerification(): Boolean {
        val user = auth.currentUser ?: throw Exception()
        var result = false
        user.sendEmailVerification()
            .addOnCompleteListener { task -> result = task.isSuccessful }
            .await()
        return result
    }

    override suspend fun login(email: String, password: String): Boolean {
        var result = false
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task -> result = task.isSuccessful }
            .await()
        return result
    }

    override suspend fun updatePassword(newPassword: String): Boolean {
        val user = auth.currentUser ?: throw Exception()
        var result = false
        user.updatePassword(newPassword)
            .addOnCompleteListener { task -> result = task.isSuccessful }
            .await()
        return result
    }
}