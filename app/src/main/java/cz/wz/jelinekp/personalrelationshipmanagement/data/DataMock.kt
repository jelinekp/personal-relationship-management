package cz.wz.jelinekp.personalrelationshipmanagement.data

import java.util.Date

data class Contact (
	val name: String,
	val lastContacted: Date,
	val country: String
)

val contacts = listOf<Contact>(
	Contact("Pavel Jelínek", Date(122, 10, 3), "Česko"),
	Contact("Honza Nový", Date(122, 4, 3), "Austrálie"),
	Contact("Pepa Hrdobec", Date(121, 10, 3), "Taiwan"),
	Contact("Aneta Nováková", Date(122, 10, 23), "Somaliland"),
)

// TODO more advanced localised date, date shortcuts (today, yesterday, days ago)
fun Date.extractDateForUi() : String {
	return day.toString() + ". " + month.toString() + ". " + (year + 1900).toString()
}