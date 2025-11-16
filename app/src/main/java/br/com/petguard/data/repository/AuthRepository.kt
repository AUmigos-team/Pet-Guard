package br.com.petguard.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.Period

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    fun registerCommonUser(
        name: String,
        birthDate: String,
        cpf: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (!isOver18(birthDate)) {
            onError("Usuário deve ter mais de 18 anos")
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener

                    val userData = hashMapOf(
                        "name" to name,
                        "birthDate" to birthDate,
                        "cpf" to cpf,
                        "email" to email,
                        "userType" to "COMMON"
                    )

                    db.collection("users")
                        .document(userId)
                        .set(userData)
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener { e ->
                            onError("Erro ao salvar o usuário: ${e.message}")
                        }

                } else {
                    val error = when (task.exception) {
                        is FirebaseAuthUserCollisionException ->
                            "Email ja cadastrado."
                        else -> task.exception?.message ?: "Erro desconhecido."
                    }
                    onError(error)
                }
            }


    }

    fun registerInspectorUser(
        name: String,
        registration: String,
        cpf: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener

                    val userData = hashMapOf(
                        "name" to name,
                        "registration" to registration,
                        "cpf" to cpf,
                        "email" to email,
                        "userType" to "INSPECTOR"
                    )

                    db.collection("users")
                        .document(userId)
                        .set(userData)
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener { e ->
                            onError("Erro ao salvar o usuário: ${e.message}")
                        }

                } else {
                    val error = when (task.exception) {
                        is FirebaseAuthUserCollisionException ->
                            "Email já cadastrado."
                        else -> task.exception?.message ?: "Erro desconhecido."
                    }
                    onError(error)
                }
            }
    }


    fun loginUser(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onError(task.exception?.message ?: "Login falhou.")
                }
            }
    }

    fun getCurrentUserData(
        onSuccess: (Map<String, Any>) -> Unit,
        onError: (String) -> Unit
    ) {
        val user = auth.currentUser ?: return onError("Nenhum usuário logado.")

        db.collection("users").document(user.uid)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    onSuccess(document.data ?: emptyMap())
                } else {
                    onError("Dados do usuário não encontrados.")
                }
            }
            .addOnFailureListener { e ->
                onError("Erro ao buscar dados: ${e.message}")
            }
    }

    private fun isOver18(birthDate: String): Boolean {
        return try {
            val parts = birthDate.split("/")

            if (parts.size != 3) return false
            if (parts.any { it.length != 2 && it.length != 4 }) return false

            val day = parts[0].toInt()
            val month = parts[1].toInt()
            val year = parts[2].toInt()

            val birth = LocalDate.of(year, month, day)
            val today = LocalDate.now()
            val limitDate = today.minusYears(18)

            !birth.isAfter(limitDate)
        } catch (e: Exception) {
            false
        }
    }
}
