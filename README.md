## Fake Open AI
This project aims to harness the capabilities of OpenAI's advanced language model for mobile applications. Like OpenAI, this AI enables natural language understanding and generation, offering users conversational interfaces and intelligent responses. Leveraging OpenAI's technology, the mobile AI application utilizes cutting-edge natural language processing to enrich user interactions and deliver valuable insights.
## MVP (Model-View-Presenter)
### Model
```kotlin
data class ChatModel(
    private val role: String,
    private var response: String,
    var isLoading: Boolean = false
) {
    fun getRole() = role
    fun getResponse() = response
    fun setResponse(newResponse: String) {
        response = newResponse
    }
}
```
### View
```kotlin
interface Interface {
    interface View:Interface.View {
    }

    interface Presenter:Interface.Presenter {
    }
}
```
### Presenter
```kotlin
class Presenter(view: MainActivity, context: Context): Interface.Presenter {
    private val view: MainActivity = view
    private val context: Context = context
}
```
## MVVM (Model-View-ViewModel)
### API
```kotlin
interface API {
    @GET("openchat.php")
    fun chat(
        @Query("text") text: String?
    ): Observable<Response>
}
```
### RetroInstance
```kotlin
object RetroInstance {
    private const val BASE_URL = "URLs are sensitive."

    fun getRetroInstance(): Retrofit {
        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }
}
```
### ViewModel
```kotlin
class ViewModel : ViewModel() {
    var chatLiveData: MutableLiveData<String?> = MutableLiveData()

    fun getChatObserver(): MutableLiveData<String?> {
        return chatLiveData
    }

    @SuppressLint("CheckResult")
    fun chat(userRequest: Request) {
        val retroService = RetroInstance.getRetroInstance().create(API::class.java)
        retroService.chat(userRequest.text)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getChatRx())
    }

    private fun getChatRx(): Observer<Response> {
        return object : Observer<Response> {
            override fun onSubscribe(d: Disposable) {
                Log.d("Sign IN", "onSubscribe: ")
            }

            override fun onNext(t: Response) {
                chatLiveData.postValue(t.response) // Assuming t.response is a string
                Log.d("test", "test1: $t")
            }

            override fun onError(e: Throwable) {
                when (e) {
                    is HttpException -> {
                        val errorBody = e.response()?.errorBody()?.string()
                        Log.e("Fetch Response", "HTTP Error occurred: ${e.code()} ${e.message()} - $errorBody")
                    }
                    is SocketTimeoutException -> {
                        Log.e("Fetch Response", "Timeout Error: ${e.message}")
                    }
                    is IOException -> {
                        Log.e("Fetch Response", "Network Error: ${e.message}")
                    }
                    else -> {
                        Log.e("Fetch Response", "Unknown Error: ${e.message}")
                    }
                }
                chatLiveData.postValue(null)
            }

            override fun onComplete() {
                Log.d("Sign IN", "onComplete: ")
            }
        }
    }
}
```
### View (MainActivity)
```kotlin
class MainActivity : AppCompatActivity(), Interface.View {
    
    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.getChatObserver().observe(this) { response ->
            response?.let {
                // Find the last message that is still loading and update it
                val position = chatAdapter.itemCount - 1
                val loadingChat = chatAdapter.chats[position]
                loadingChat.setResponse(it)
                loadingChat.isLoading = false
                chatAdapter.updateChat(position, loadingChat)
            }
        }
    }

    private fun chat() {
        val request = Request().apply {
            text = editText.text.toString()
        }
        viewModel.chat(request)
        editText.text.clear()
    }
}
```
## Explanation
**MVP**: In MVP, the Presenter acts as an intermediary between the View and the Model. It handles data logic and updates the View.
**MVVM**: MVVM separates the View (UI) from the business logic (ViewModel). The ViewModel manages the data for the View and maintains state across configuration changes.
