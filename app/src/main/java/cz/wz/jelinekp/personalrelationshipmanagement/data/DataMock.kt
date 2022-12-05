package cz.wz.jelinekp.personalrelationshipmanagement.data

import java.util.Date

class Contact (
	val name: String,
	val lastContacted: Date
)

val contacts = listOf<Contact>(
	Contact("Pavel Jelínek", Date(122, 10, 3)),
	Contact("Honza Nový", Date(122, 4, 3)),
	Contact("Pepa Hrdobec", Date(121, 10, 3)),
	Contact("Aneta Nováková", Date(122, 10, 23)),
)