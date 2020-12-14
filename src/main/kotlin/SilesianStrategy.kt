import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.timerTask
import kotlin.system.exitProcess


class SilesianStrategy {
    private val buildings =  BuildingsContainer()
    var money: Int = 2000
    private val timer = Timer()
    private val gameSaveManager = GameSaveManager()
    private val sdf = SimpleDateFormat("dd.MM.yyyy-HH:mm:ss")

    fun saveGame(){
        gameSaveManager.add(GameSave(money, buildings.getBuildingsCopy(), sdf.format(Date())))
    }
    fun loadGame(index: Int){
        val save = gameSaveManager.get(index)
        money = save.money
        buildings.setBuildings(save.buildings)
    }
    fun loadGame(save: GameSave){
        money = save.money
        buildings.setBuildings(save.buildings)
    }

    fun getLatestSave() = gameSaveManager.get(0)

    fun showSaves(): Int{
        gameSaveManager.showSaves().forEachIndexed {i, it->
            println("%d - %s".format(i, it))
        }
        println("q - Pociepnij wgranie")
        return gameSaveManager.showSaves().size
    }

    fun getBuildings(): ArrayList<Building> = buildings.getBuildings()

    @Synchronized
    fun updateMoney(revenue: Int, amount: Int){
        money += revenue * amount
    }

    fun initBuildingThreads(){
        buildings.getBuildings().forEach {
            timer.scheduleAtFixedRate(timerTask {
               updateMoney(it.revenue, it.amount)
            }, it.interval.toLong(), it.interval.toLong())
        }
    }

    fun buy(buildingName: String, amount: Int = 1){
        val seller: BuildingSeller = if(buildingName in listOf("chatka drwala", "kamieniołom")){
            BasicBuildingSeller(buildingName, amount, buildings, money)
        } else{
            BuildingSeller(buildingName, amount, buildings, money)
        }
        money = seller.buy()
    }
}

fun main(args: Array<String>) {
    val s = SilesianStrategy()
    GlobalScope.launch {
        s.initBuildingThreads()
    }
    var save: GameSave? = null
    try{
        val zapis = File("zapis.json").readText()
        save = Json.decodeFromString<GameSave>(zapis)
    }catch (ex:Exception){}
    if(save!=null){
        s.loadGame(save)
    }
    println("Witej we szpilu strategicznyj Silesian Strategy!")
    while(true){
        println("Ôbier ôpcyjõ")
        println("1 - Kup budōnek\n" +
                "2 - Pokŏż stan pijynżny\n" +
                "3 - Pokŏż stan budōnkōw\n" +
                "4 - Zapisz szpil\n" +
                "5 - Wgrej szpil\n" +
                "0 - Wyjdź z szpilu")
        var menuOption = readLine().toString()
        menuOption = if (menuOption.isEmpty()) "9" else menuOption
        when(menuOption.first()){
            '1'->{
                println("Ôbier budōnek")
                s.getBuildings().forEachIndexed { i: Int, building: Building ->
                    println("%c - %s %d".format((i+97).toChar(), building.name, building.price))
                }
                println("0 - Pociepnij sprŏwōnek")
                var buildingOption = readLine().toString()
                buildingOption = if (buildingOption.isEmpty()) "9" else buildingOption
                fun isInRange(c:Char): Boolean{
                    val range = 'a'.rangeTo('z')
                    return c in range
                }
                when{
                    isInRange(buildingOption.first()) ->{
                        val index = buildingOption.first().toInt() - 97
                        s.buy(s.getBuildings()[index].name)
                    }
                    buildingOption.first() == '0'->{
                        println("Sprŏwōnek pociepniynto było")
                    }
                    else->{
                        println("Niy znōm takij ôpcyje")
                    }
                }
            }
            '2'->{
                println("Posiadŏsz %d kōnsztōw mōnet".format(s.money))
            }
            '3'->{
                s.getBuildings().forEach {
                    println("%s: %d".format(it.name, it.amount))
                }
            }
            '4'->{
                s.saveGame()
                println("Zapisano było szpil")
            }
            '5'->{
                println("Ôbier szkryft do wgraniŏ")
                val last = s.showSaves() - 1
                var saveOption = readLine().toString()
                saveOption = if (saveOption.isEmpty()) "a" else saveOption
                fun isInRange(c:Char): Boolean{
                    val range = '0'.rangeTo(last.toString()[0])
                    return c in range
                }
                when{
                    isInRange(saveOption.first()) ->{
                        s.loadGame(saveOption.toInt())
                        println("wczytuję %c".format(saveOption.first()))
                    }
                    saveOption.first() == 'q'->{
                        println("Wgrŏwanie pociepniynto było")
                    }
                    else->{
                        println("Niy znōm takij ôpcyje")
                    }
                }
            }
            '0'->{
                println("Żegnej")
                s.saveGame()
                val zapis = Json.encodeToString(s.getLatestSave())
                File("zapis.json").writeText(zapis)
                exitProcess(0)
            }
            else->{
                println("Niy znōm takij ôpcyje")
            }
        }
    }
}