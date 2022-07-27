package uz.pdp.unsplashfullwallpapers.room.doa

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import uz.pdp.unsplashfullwallpapers.room.entity.TopicModel

@Dao
interface TopicDao {

    @Insert(onConflict = REPLACE)
    fun addTopic(topicList: List<TopicModel>)

    @Query("select * from topicmodel")
    fun getAllTopic(): List<TopicModel>
}