package com.raisc.dnaapp.data


import androidx.paging.PagingSource
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.raisc.dnaapp.model.Project


@Dao
interface PendingProjectsDao {

    /**
     * Returns all data in table for Paging
     *
     * @param query a dynamic SQL query
     */
    @RawQuery(observedEntities = [Project::class])
    fun getAll(query: SupportSQLiteQuery?): PagingSource<Int, Project>

    /**
     * Returns a Tea based on the tea name.
     *
     * @param name of a tea
     */
    @Query("SELECT * from projects_table")
    fun getPendingProjects(): PagingSource<Int, Project>

//    @Query("SELECT * from projects_table")
//    fun getPendingProjects(): LiveData<List<Project>>
//
    @Query("SELECT * from projects_table where projectName =:name")
    fun getPendingProject(name:String): Project
//
//    @Query("SELECT * from projects_table")
//    fun getcompleteProjects(): LiveData<List<Project>>
//
//    @Query("SELECT * from projects_table")
//    fun getNewProjects(): LiveData<List<Project>>
//
//    @Query("SELECT * from projects_table")
//    fun getProjects(): LiveData<List<Project>>

    /**
     * Update tea if its favorite or not.
     *
     * @param name of a tea
     */
    @Update
    fun updateFavorite(name: List<Project>)

    /**
     * Returns a random Tea
     */
//    @Query("SELECT * FROM barItems_table ORDER BY RANDOM()")
//    fun getRandomTea(): BarItem?
//
//    @Query("SELECT * from barItems_table ORDER BY id DESC LIMIT 1")
//    fun getRecentTea(): LiveData<BarItem?>?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(project: Project)

    @Delete
    fun delete(project: Project?)

//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    fun saveProject(project: Project)
//    override fun getBarClub(): LiveData<Bars> {
//        TODO("Not yet implemented")
//    }
}