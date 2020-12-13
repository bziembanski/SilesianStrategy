import kotlinx.serialization.*

@Serializable
data class Building(
    val name: String,
    val price: Int,
    val revenue: Int,
    val interval: Int,
    val required: Array<String>,
    var amount: Int
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Building

        if (name != other.name) return false
        if (price != other.price) return false
        if (revenue != other.revenue) return false
        if (interval != other.interval) return false
        if (!required.contentEquals(other.required)) return false
        if (amount != other.amount) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + price
        result = 31 * result + revenue
        result = 31 * result + interval
        result = 31 * result + required.contentHashCode()
        result = 31 * result + amount
        return result
    }
}
