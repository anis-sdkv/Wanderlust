package com.wanderlust.data.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.wanderlust.domain.action_results.LoginResult
import com.wanderlust.domain.action_results.RegisterResult
import com.wanderlust.domain.repositories.UserRepository
import com.wanderlust.domain.services.UserService
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class UserService @Inject constructor(private val auth: FirebaseAuth, private val repository: UserRepository) :
    UserService {
    val currentUser get() = auth.currentUser

    override suspend fun register(username: String, email: String, password: String): RegisterResult {
        return try {
            val id = auth.createUserWithEmailAndPassword(email, password)
                .await()
                .user?.uid ?: throw IllegalArgumentException("Unknown auth exception")

            repository.createUser(id, username)
            RegisterResult.SuccessRegister(id)
        } catch (e: FirebaseAuthException) {
            RegisterResult.FailRegister(e.message)
        } catch (e: FirebaseFirestoreException) {
            currentUser?.delete()?.await()
            RegisterResult.FailRegister("Не удалось завершить регистрацию, попробуйте еще раз.")
        } catch (e: Exception) {
            RegisterResult.FailRegister(e.message)
        }
    }

    override suspend fun sendVerification(): Boolean {
//        val user = auth.currentUser ?: throw Exception()
//        var result = false
//        user.sendEmailVerification()
//            .addOnCompleteListener { task -> result = task.isSuccessful }
//            .await()
//        return result
        TODO()
    }

    override suspend fun login(email: String, password: String): LoginResult {
        return try {
            val id = auth.signInWithEmailAndPassword(email, password)
                .await()
                .user?.uid ?: throw IllegalArgumentException("Unknown auth exception")

            LoginResult.SuccessLogin(id)
        } catch (e: FirebaseAuthException) {
            LoginResult.FailLogin(e.message)
        }
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