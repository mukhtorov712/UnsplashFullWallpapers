package uz.pdp.unsplashfullwallpapers.room.doa

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import uz.pdp.unsplashfullwallpapers.room.entity.ImageEntity

@Dao
interface ImageDoa {

    @Insert
    fun addImage(imageEntity: ImageEntity)

    @Query("DELETE FROM imageentity WHERE id = :imageId")
    fun deleteImage(imageId: String)

    @Query("select count(id) from imageentity where id like :id")
    fun getIsLiked(id: String): Int

    @Query("select * from imageentity")
    fun getAllImage(): List<ImageEntity>
}