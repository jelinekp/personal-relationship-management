package cz.wz.jelinekp.prm.features.categories.data.db

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation
import cz.wz.jelinekp.prm.features.categories.model.Category
import cz.wz.jelinekp.prm.features.contacts.data.db.DbContact
import cz.wz.jelinekp.prm.features.contacts.model.Contact

@Entity(tableName = "contact_category", primaryKeys = ["id", "category_name"])
data class DbContactCategory(
	
	@ColumnInfo(name = "id")
	val contactId: Long,
	
	@ColumnInfo(name = "category_name")
	val categoryName: String,
	
	)

data class DbContactWithDbCategories(
	@Embedded val contact: DbContact,
	@Relation(
		parentColumn = "id",
		entityColumn = "category_name",
		associateBy = Junction(DbContactCategory::class)
	)
	val categories: List<DbCategory>
) {
	fun toContactWithCategories(): ContactWithCategories {
		return ContactWithCategories(
			contact = contact.toContact(),
			categories = categories.map { it.toCategory() },
		)
	}
}

data class DbCategoryWithDbContacts(
	@Embedded val category: DbCategory,
	@Relation(
		parentColumn = "category_name",
		entityColumn = "id",
		associateBy = Junction(DbContactCategory::class)
	)
	val contacts: List<DbContact>
)

data class ContactWithCategories(
	val contact: Contact,
	val categories: List<Category>
)

data class CategoryWithContacts(
	val category: Category,
	val contacts: List<Contact>
)
