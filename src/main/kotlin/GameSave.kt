import kotlinx.serialization.Serializable

@Serializable
data class GameSave(val money: Int, val buildings: ArrayList<Building>, val date: String)
