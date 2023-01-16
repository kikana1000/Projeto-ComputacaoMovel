package pt.ipp.estg.doctorbrain.data

import androidx.lifecycle.LiveData
import pt.ipp.estg.doctorbrain.models.HourDateSchoolClass
import pt.ipp.estg.doctorbrain.models.SchoolClass

/**
 * Implementação do DAO(Data Access Object) dos dia/hora quando a schoolClass acontece
 */
class SchoolClassRepository(val schoolClassDao: SchoolClassDao) {

    fun insertSchoolClass(newSchoolClass: SchoolClass): Long =
        schoolClassDao.insertSchoolClass(newSchoolClass)

    suspend fun updateSchoolClass(newSchoolClass: SchoolClass) {
        schoolClassDao.updateSchoolClass(newSchoolClass)
    }

    fun deleteAll() {
        schoolClassDao.deleteAllSchoolClass()
    }

    fun deleteSchoolClass(schoolClass: SchoolClass) {
        schoolClassDao.deleteSchoolClass(schoolClass)
    }

    fun findbyId(id: Int): LiveData<List<SchoolClass>> {
        return schoolClassDao.findSchoolClass(id)
    }

    fun search(search: String): LiveData<List<SchoolClass>> {
        return schoolClassDao.searchSchoolClasses(search)
    }

    fun getSchoolClasses(): LiveData<List<SchoolClass>> {
        return schoolClassDao.getAllSchoolClass()
    }

}