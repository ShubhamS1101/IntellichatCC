package data

data class UserData(

    var userId: String?="",
    var name: String?="",
    var number: String?="",
    var imageurl: String?=""
)
{

    fun toMap()= mapOf(
        "userId" to userId,
        "name" to name,
        "number" to number,
        "imageurl" to imageurl
    )

}

data class ChatData(
    val chatId : String?="",
    val user1: ChatUser = ChatUser(),
    val user2: ChatUser = ChatUser()
)

data class ChatUser(
    val userId:String?="",
    val name:String?="",
    val imageUrl:String?="",
    val number:String?="",

)

data class Message(
    var sendBy : String?="",
    val message : String?="",
    val timestamp : String? = ""
)