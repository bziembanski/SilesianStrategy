class GameSaveManager {
    private val gameSaves: ArrayList<GameSave> = ArrayList()

    fun add(gameSave: GameSave){
        if(gameSaves.size>=SAVE_MANAGER_CAPACITY)
            gameSaves.remove(gameSaves.last())
        gameSaves.add(gameSave)
        gameSaves.sortByDescending { it.date }
    }

    fun get(index: Int) = gameSaves[index]

    fun showSaves() = gameSaves.map { it.date }

    companion object{
        const val SAVE_MANAGER_CAPACITY = 10
    }
}