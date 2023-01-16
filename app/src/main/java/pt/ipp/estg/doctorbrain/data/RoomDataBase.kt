package pt.ipp.estg.doctorbrain.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pt.ipp.estg.doctorbrain.models.*

/**
 * Instancia a base de dados de room para todos as informações que guardamos em room
 * nomeadamente SchoolClasses,HourDateSchoolClasses,Events
 */
@Database(entities = [
    (SchoolClass::class),
    (HourDateSchoolClass::class),
    (SingleEvent::class),
    (Group::class),
    (PhoneNumber::class)
    ],
    version = 20)
abstract class RoomDataBase:RoomDatabase() {
    abstract fun SchoolClassDao():SchoolClassDao
    abstract fun SingleEventDao():SingleEventDao
    abstract fun HourDateDao():HourDateDao
    abstract fun GroupDao():GroupDao
    abstract fun PhoneDao():PhoneDao

    companion object{
        private var INSTANCE:RoomDataBase? = null
        fun getInstace(context: Context):RoomDataBase{
            synchronized(this){
                var instance = INSTANCE
                if (instance==null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RoomDataBase::class.java,
                        "Room_Database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}