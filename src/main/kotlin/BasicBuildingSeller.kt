class BasicBuildingSeller
    (
    buildingName: String,
    amount: Int,
    buildingsContainer: BuildingsContainer,
    money: Int
) : BuildingSeller(buildingName, amount, buildingsContainer, money) {


    override fun checkRequired() {
        requiredCheck = true
    }
}