package pt.ipp.estg.doctorbrain.firestoreFun

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import pt.ipp.estg.doctorbrain.App
import pt.ipp.estg.doctorbrain.models.*
import pt.ipp.estg.doctorbrain.models.enums.State.Closed
import pt.ipp.estg.doctorbrain.models.enums.State.Open
import pt.ipp.estg.doctorbrain.notifications.Questions.RetroFitInstance
import pt.ipp.estg.doctorbrain.notifications.pushNotification.channelId
import java.util.*

/**
 * Modelo de view da SchoolClass
 */
class FireBaseViewModel(application: Application) : AndroidViewModel(application) {

    val TAG = "FireBaseViewModel"

    val db: FirebaseFirestore = Firebase.firestore
    val auth: FirebaseAuth = Firebase.auth

    private var _userProfile: MutableLiveData<User> = MutableLiveData<User>()
    var userProfile: LiveData<User> = _userProfile

    private var _questionsList: MutableLiveData<List<Question>> = MutableLiveData<List<Question>>(
        listOf()
    )
    var questionsList: LiveData<List<Question>> = _questionsList

    private var _selectedQuestion: MutableLiveData<Question> = MutableLiveData<Question>()
    var selectedQuestion: LiveData<Question> = _selectedQuestion

    init {
        if (auth.currentUser != null) {
            _userProfile.value = getUserProfileById().value
             }
        //getQuestions()
    }


    /**
     * Faz SignIn do User
     */
    fun signInUser(email: String, password: String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email, password)

    }


    /**
     * Faz SignUp do User e chama funcao pra criar UserProfile
     */
    fun signUpUser(
        name: String, email: String, contact: String, password: String, studentNumber: String
    ): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val currentUserID = it.user?.uid
                if (currentUserID != null) {
                    addUserProfile(
                        User(
                            currentUserID, name, email, contact, password, studentNumber
                        )
                    )
                }

            }
    }


    /**
     * Adiciona utilizador na fireStore
     * @param db base de dados da firebase
     * @param user user que sera adicionado
     */
    fun addUserProfile(user: User) {
        db.collection("users")
            .add(user)

    }

    /**
     * Saves userProfile in LiveDataVariable
     */
    fun getUserProfile() {
        val currentUserID = auth.currentUser?.uid

        if (currentUserID != null) {
            val userDocuments = db.collection("users").whereEqualTo("id", currentUserID)

            viewModelScope.launch(Dispatchers.IO) {
                userDocuments.get().addOnSuccessListener { results ->
                    val userList = results.toObjects(User::class.java)
                    if (userList.isNotEmpty()) {
                        _userProfile.value = userList.first()
                    }
                }
            }
        }
    }

    /**
     * Saves userProfile in LiveDataVariable
     * Talvez substitua o metodo getUserProfile (2 em 1)
     */
    fun getUserProfileById(profileId: String = auth.currentUser?.uid.toString()): MutableLiveData<User> {
        val user: MutableLiveData<User> = MutableLiveData()

        val userDocuments = db.collection("users").whereEqualTo("id", profileId)

        viewModelScope.launch(Dispatchers.Main) {
            val userList = userDocuments.get().await().toObjects(User::class.java)
            if (userList.isNotEmpty()) {
                if (profileId.equals(auth.currentUser?.uid.toString())) {
                    _userProfile.value = userList.first()
                }
                withContext(Dispatchers.Main) {
                    user.postValue(userList.first())
                }
            }
        }
        return user
    }

    /**
     * Checks if it exists a user profile with a certain phone number
     * If it does, returns an observable with the user profile as value
     */
    fun getUserProfileByPhoneNumber(phoneNumber: String): MutableLiveData<User> {
        val user: MutableLiveData<User> = MutableLiveData()

        val userDocuments = db.collection("users").whereEqualTo("contacto", phoneNumber)
        viewModelScope.launch(Dispatchers.Main) {
            val userList = userDocuments.get().await().toObjects(User::class.java)
            if (userList.isNotEmpty()) {
                withContext(Dispatchers.Main) {
                    user.postValue(userList.first())
                }
            }
        }
        return user
    }

    /**
     * Metodo crasha ... recebe objeto misterioso ...
     */
    fun getUsers(): MutableLiveData<List<User>> {
        var users: MutableLiveData<List<User>> = MutableLiveData()

        viewModelScope.launch(Dispatchers.IO) {
            val userDocuments = db.collection("users")
            viewModelScope.launch(Dispatchers.Main) {
                val userList = userDocuments.get().await().toObjects(User::class.java)
                if (userList.isNotEmpty()) {
                    users.value = userList
                }
            }
        }
        return users
    }


    /**
     * Adiciona/update questão na firebase
     * Se passado ID, atualiza
     * @param db base de dados da firebase
     * @param question questão que sera adicionada
     */
    fun addQuestion(
        question: Question,
        id: String = UUID.randomUUID().toString()
    ): Task<Void>? {
        if (question == null) return null
        if (userProfile.value == null) return null
        // Add a new document with a generated ID
        return db.collection("questions")
            .document(id).set(
                Question(
                    OwnerID = if (question.OwnerID != ("myOwnerID")) question.OwnerID else userProfile.value!!.id, //se a question tiver owner, entao mantemos ele, caso contrario, colocamos o atual
                    QuestionID = id,
                    Title = question.Title,
                    Description = question.Description,
                    Answers = question.Answers,
                    Participants = question.Participants,
                    IsPutlic = question.IsPutlic,
                    State = question.State
                    //dateAndTime
                    //groups
                )
            ).addOnCompleteListener { if(!question.Participants.isEmpty()) {question.Participants.forEach{
                    QuestionNotification(
                        Notification("You have been added in ${question.Title} !!" , question.Description),
                        Data(Firebase.auth!!.uid!!,question.QuestionID),"/topics/user_${it}"
                    )
                }
            } }

    }

    /**
     * Saves questions in LiveDataVariable
     * @param db base de dados da firebase
     * @param questionsListList lista de question que ira ser preenchida
     */
    fun getQuestions(thatImTheOwnerOf: Boolean = false) {
        var questionsDocuments = db.collection("questions")

        viewModelScope.launch(Dispatchers.IO) {
            questionsDocuments.addSnapshotListener { results, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                val receivedQuestionsList: MutableList<Question> = mutableListOf()
                //para cada documento recebido, vamos construir uma question e guarda la numa lista
                if (results != null) {
                    for (result in results) {
                        val questionData = result.data
                        val question: Question = buildQuestionFromData(questionData)

                        //se quiser saber as questoes das quais sou dono, entao vai verificar se sou dono, caso contrario, verifica o resto
                        if (thatImTheOwnerOf) {
                            if (isCurrentUserTheQuestionOwner(question)) {
                                receivedQuestionsList.add(
                                question)
                                FirebaseMessaging.getInstance().subscribeToTopic("${question.QuestionID}")

                            }else{
                                FirebaseMessaging.getInstance().unsubscribeFromTopic("${question.QuestionID}")
                            }
                        } else {
                            //se for participante, adiciona as questoes
                            if (isCurrentUserAParticipant(question)){
                                receivedQuestionsList.add(question)
                                FirebaseMessaging.getInstance().subscribeToTopic("${question.QuestionID}")
                            }else{
                                FirebaseMessaging.getInstance().unsubscribeFromTopic("${question.QuestionID}")
                            }
                        }
                    }
                }
                //se a lista de questions nao estiver vazia, vamos guardar ela na liveData
                if (receivedQuestionsList.isNotEmpty()) {
                    _questionsList.value = receivedQuestionsList.toList()
                }
            }
        }
        // }
    }

    /**
     * Gets Question from db to liveData
     */
    fun getQuestionByIdBackUp(
        selectedQuestionID: String,
    ) {
        //val currentUserID = auth.currentUser?.uid

        //if (currentUserID != null) {
        val questionsDocuments =
            db.collection("questions").whereEqualTo("questionID", selectedQuestionID)

        viewModelScope.launch(Dispatchers.IO) {
            questionsDocuments.get().addOnSuccessListener { results ->
                val receivedQuestionsList: MutableList<Question> = mutableListOf()
                //para cada documento recebido, vamos construir uma question e guarda la numa lista
                for (result in results) {
                    val questionData = result.data
                    val question: Question = buildQuestionFromData(questionData)
                    receivedQuestionsList.add(question)
                }
                //se a lista de questions nao estiver vazia, vamos guardar ela na liveData
                if (receivedQuestionsList.isNotEmpty()) {
                    _selectedQuestion.value = receivedQuestionsList.first()}
            }
        }
        //}
    }

    /**
     * Gets Question from db to liveData
     */
    fun getQuestionById(
        selectedQuestionID: String,
    ) {
        //val currentUserID = auth.currentUser?.uid

        //if (currentUserID != null) {
        val questionsDocuments =
            db.collection("questions").whereEqualTo("questionID", selectedQuestionID)

        viewModelScope.launch(Dispatchers.IO) {
            questionsDocuments.addSnapshotListener { results, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                val receivedQuestionsList: MutableList<Question> = mutableListOf()
                //para cada documento recebido, vamos construir uma question e guarda la numa lista
                if (results != null) {
                    for (result in results) {
                        val questionData = result.data
                        val question: Question = buildQuestionFromData(questionData)
                        receivedQuestionsList.add(question)
                    }
                }
                //se a lista de questions nao estiver vazia, vamos guardar ela na liveData
                if (receivedQuestionsList.isNotEmpty()) {
                    _selectedQuestion.value = receivedQuestionsList.first()
                }
            }
        }
    }

    /**
     * Builds the Question From Data received from Firebase
     */
    private fun buildQuestionFromData(questionData: MutableMap<String, Any>): Question {
        try {
            val receivedAnswers: MutableList<Answer> = mutableListOf()
            val receivedAnswersData: List<MutableMap<String, Any>> =
                questionData.get("answers") as List<MutableMap<String, Any>>
            for (answerData: MutableMap<String, Any> in receivedAnswersData) {
                receivedAnswers.add(
                    Answer(
                        _answerID = answerData.get("_answerID") as String,
                        _ownerID = answerData.get("_ownerID") as String,
                        Description = answerData.get("description") as String,
                        //DateHour
                    )
                )
            }
            return Question(
                QuestionID = questionData.get("questionID") as String,
                OwnerID = questionData.get("ownerID") as String,
                Title = questionData.get("title") as String,
                Description = questionData.get("description") as String,
                Answers = receivedAnswers,
                Participants = questionData.get("participants") as MutableList<String>,
                IsPutlic = questionData.get("isPutlic") as Boolean,
                State = if((questionData.get("state") as String).equals("Open")) Open else Closed
            )
        } catch (e: Exception) {
            return Question()
        }
    }

    /**
     * Verifica se o current tem acesso à question selecionada ou passada por parametro
     * Devolve Boolean
     */
    private fun isCurrentUserAParticipant(question: Question? = selectedQuestion.value): Boolean {

        if (question == null) return false
        if (question.IsPutlic) return true
        if (auth.currentUser == null) return false
        if (question.OwnerID == auth.currentUser!!.uid.toString()) return true
        if (question.Participants.isEmpty()) return false
        if (question.Participants.contains(auth.currentUser!!.uid.toString())) return true
        return false
    }

    /**
     * Verifica se o current tem acesso à question selecionada ou passada por parametro
     * Devolve Boolean
     */
    fun isCurrentUserTheQuestionOwner(question: Question? = selectedQuestion.value): Boolean {

        if (question == null) return false
        if (auth.currentUser == null) return false
        if (question.OwnerID == auth.currentUser!!.uid.toString()) return true
        return false
    }

    /**
     * Adds answer to question in firebase and liveData
     * So consegue responder se estiver Logado
     */
    fun addAnswerToQuestion(answer: Answer) {
        if (isCurrentUserAParticipant() == false) return
        if (answer == null) return
        if (selectedQuestion.value == null) return
        if (userProfile.value == null) return

        val updatedQuestion: Question = selectedQuestion.value!!

        //Adiciona a answer a updatedQuestion e depois mandar a question para a base de dados e depois mete la na liveData
        updatedQuestion.Answers.add(
            Answer(
                _answerID = UUID.randomUUID().toString(),
                _ownerID = userProfile.value!!.id,
                Description = answer.Description,
                //dateAndHour NOW
            )
        )
        addQuestion(updatedQuestion, updatedQuestion.QuestionID)?.addOnSuccessListener {
            _selectedQuestion.value = updatedQuestion
        }

        var uid= userProfile.value!!.id
        var name= userProfile.value!!.name
        var id=selectedQuestion.value!!.QuestionID
       sendNotification(
            QuestionNotification(
                Notification("New answer in " + selectedQuestion!!.value!!.Title!!, "($name): "+answer.Description),
                Data(uid,id),"/topics/$id"
            )
        )

    }

    private fun sendNotification(notification: QuestionNotification)= CoroutineScope(Dispatchers.IO).launch {
        try{
            val response = RetroFitInstance.api.postNotification(notification)

        }catch (e: java.lang.Exception){
            Log.d("Pior que FALHANÇO!!",e.message!!)
        }
    }

    /**
     * Muda o IsPublic da questao selecionada
     * So consegue se estiver Logado
     */
    fun changeIsPrivateQuestion() {
        if (selectedQuestion.value == null) return
        if (userProfile.value == null) return

        val updatedQuestion: Question = selectedQuestion.value!!

        //faz alteracoes  na updatedQuestion e depois mandar a question para a base de dados e depois mete la na liveData
        updatedQuestion.IsPutlic = !(updatedQuestion.IsPutlic)

        addQuestion(updatedQuestion, updatedQuestion.QuestionID)?.addOnSuccessListener {
            _selectedQuestion.value = updatedQuestion
        }
    }

    /**
     * Muda o IsPublic da questao selecionada
     * So consegue se estiver Logado
     */
    fun changeQuestionState() {
        if (selectedQuestion.value == null) return
        if (userProfile.value == null) return

        val updatedQuestion: Question = selectedQuestion.value!!

        //faz alteracoes  na updatedQuestion e depois mandar a question para a base de dados e depois mete la na liveData
        updatedQuestion.State = if(updatedQuestion.State == Open) Closed else Open

        addQuestion(updatedQuestion, updatedQuestion.QuestionID)?.addOnSuccessListener {
            _selectedQuestion.value = updatedQuestion
        }
    }

    /**
     * Sets Question to liveData
     */
    fun setSelectedQuestion(
        selectedQuestion: Question,
    ) {
        _selectedQuestion.value = selectedQuestion
    }

}