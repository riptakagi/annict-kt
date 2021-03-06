package jp.annict.enums

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

/**
 * Rating state
 *
 * @property locale
 * @constructor Create empty Rating state
 */
@Serializable
enum class RatingState(val locale: String) {

    @SerialName("bad") BAD("良くない"),
    @SerialName("average") AVERAGE("普通"),
    @SerialName("good") GOOD("良い"),
    @SerialName("great") GREAT("とても良い");
}