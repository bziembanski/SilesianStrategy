open class BuildingSeller(
    protected val buildingName: String,
    protected val amount: Int,
    protected val buildingsContainer: BuildingsContainer,
    protected var money: Int
) {
    protected var nameCheck: Boolean = false
    protected var requiredCheck: Boolean = false
    protected var moneyCheck: Boolean = false

    fun buy(): Int {
        checkNameCorrectness()
        checkRequired()
        checkMoney()
        decreaseMoney()
        incrementBuildingAmount()
        return money
    }

    open fun checkNameCorrectness() {
        nameCheck = buildingsContainer.checkName(buildingName)
    }

    open fun checkRequired() {
        var check = 0
        val building = buildingsContainer.getBuilding(buildingName)
        if (nameCheck) {
            building.required.forEach {
                if (buildingsContainer.getBuilding(it).amount > 0)
                    check++
            }
            requiredCheck = check == building.required.size
            if(!requiredCheck){
                print("Do wybudowaniŏ tego budōnku potrzebujesz: ")
                building.required.forEach {
                    print("[%s]".format(it))
                }
                println()
            }
        }
    }

    open fun checkMoney() {
        val building = buildingsContainer.getBuilding(buildingName)
        if (requiredCheck) {
            val price = building.price
            moneyCheck = (price * amount) <= money
            if (!moneyCheck){
                println("Do wybudowaniŏ tego budōnkōw potrzebujesz %d kōnsztōw złotŏ".format(building.price))
            }
        }
    }

    open fun decreaseMoney() {
        if (moneyCheck) {
            val price = buildingsContainer.getBuilding(buildingName).price
            money -= (price * amount)
        }
    }

    open fun incrementBuildingAmount() {
        if (moneyCheck) {
            buildingsContainer.addBuilding(buildingName, amount)
        }
    }
}