import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

class BuildingsContainer(initFile: String = "buildings.json") {
    private var buildings = Json.decodeFromString<ArrayList<Building>>(File(initFile).readText(Charsets.UTF_8))

    fun addBuilding(building: String, amount: Int = 1) {
        if (building in buildings.map { it.name }) {
            buildings.first { it.name == building }.amount += amount
        }
    }

    fun setBuildings(array: ArrayList<Building>) {
        array.forEachIndexed { index, building ->
            buildings[index].amount = building.amount
        }
    }

    fun getBuildings(): ArrayList<Building> {
        return buildings
    }

    fun getBuildingsCopy(): ArrayList<Building>{
        return buildings.map { it.copy() } as ArrayList<Building>
    }

    fun getBuilding(name: String): Building {
        return buildings.first { it.name == name }
    }

    fun checkName(name: String): Boolean {
        return name in buildings.map { it.name }
    }
}