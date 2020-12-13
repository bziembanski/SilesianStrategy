import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.timerTask
import kotlin.system.exitProcess


class SilesianStrategy {
    private val buildings =  BuildingsContainer()
    var money = 2000
    private val timer = Timer()

    fun getBuildings(): ArrayList<Building> = buildings.getBuildings()

    fun getBuilding(name: String): Building = buildings.getBuilding(name)

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
    println("Witej we szpilu strategicznyj SilesianStrategy!")
    while(true){
        println("Ôbier ôpcyjõ")
        println("1 - Kup budōnek\n" +
                "2 - Pokŏż stan pijynżny\n" +
                "3 - Pokŏż stan budōnkōw\n" +
                "4 - Zapisz szpil\n" +
                "0 - Wyjdź z szpilu")
        var menuOption = readLine()
        menuOption = if (menuOption?.isEmpty() == true) "9" else menuOption
        when(menuOption?.first()){
            '1'->{
                println("Ôbier budōnek")
                s.getBuildings().forEachIndexed { i: Int, building: Building ->
                    println("%d - %s %d".format(i+1, building.name, building.price))
                }
                var buildingOption = readLine()
                buildingOption = if (buildingOption?.isEmpty() == true) "9" else buildingOption
                when(buildingOption?.first()){
                    '1'->{
                        s.buy(s.getBuildings()[0].name)
                    }
                    '2'->{
                        s.buy(s.getBuildings()[1].name)
                    }
                    '3'->{
                        s.buy(s.getBuildings()[2].name)
                    }
                    '4'->{
                        s.buy(s.getBuildings()[3].name)
                    }
                    '5'->{
                        s.buy(s.getBuildings()[4].name)
                    }
                    '0'->{
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
                println("Zapisano było szpil w folderze bazowym")
                //TODO()
            }
            '0'->{
                println("Żegnej")
                exitProcess(0)
            }
            else->{
                println("Niy znōm takij ôpcyje")
            }
        }
    }
}