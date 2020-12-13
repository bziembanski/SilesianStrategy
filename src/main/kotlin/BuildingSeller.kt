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
        var check = 0;
        val building = buildingsContainer.getBuilding(buildingName)
        if (nameCheck) {
            building.required.forEach {
                if (buildingsContainer.getBuilding(it).amount > 0)
                    check++
            }
        }
        requiredCheck = check == building.required.size
    }

    open fun checkMoney() {
        if (requiredCheck) {
            val price = buildingsContainer.getBuilding(buildingName).price
            moneyCheck = (price * amount) <= money
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