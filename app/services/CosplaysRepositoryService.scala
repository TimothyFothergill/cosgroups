package services

import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api.*
import repositories.*
import models.{Cosplay, CosplayComponent, Material}

import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration._

import utility.CurrencyColumnTypes.*
import utility.JsonColumnTypes.*

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile
import play.db.NamedDatabase
import scala.concurrent.ExecutionContext

class CosplaysRepositoryService @Inject(
    @NamedDatabase("cosgroups") val dbConfigProvider: DatabaseConfigProvider
)(implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
    import profile.api._

    private val cosplaysTable = TableQuery[CosplaysTable]
    private val insertProjection =
    cosplaysTable.map(c =>
        (c.characterName, c.cosplayer, c.seriesName, c.started, c.completed, c.description, c.budget, c.cosplayComponents)
    )

    def addNewCosplay(newCosplay: CosplaysRepositoryInsertModel): Future[Int] = {
        val action =
            insertProjection += (
                newCosplay.characterName,
                newCosplay.cosplayer,
                newCosplay.seriesName,
                newCosplay.started,
                newCosplay.completed,
                newCosplay.description,
                newCosplay.budget,
                newCosplay.cosplayComponents
            )
        db.run(action)
    }

    def returnAllCosplays(): Future[Seq[CosplaysRepositoryModel]] = {
        db.run(cosplaysTable.result)
    }

    def returnCosplaysById(id: Long): Future[Option[CosplaysRepositoryModel]] = {
        db.run(cosplaysTable.filter(_.id === id).result.headOption)
    }

    def returnCosplaysByCosplayerId(cosplayerId: Long): Future[Option[CosplaysRepositoryModel]] = {
        db.run(cosplaysTable.filter(_.cosplayer === cosplayerId).result.headOption)
    }

    def updateCosplay(updatedCosplay: CosplaysRepositoryModel): Future[Int] = {
        val query = 
            cosplaysTable.filter(_.id === updatedCosplay.id)
                .map(c => (c.characterName, c.cosplayer, c.seriesName, c.started, c.completed, c.description, c.budget, c.cosplayComponents))
                .update(
                    (
                        updatedCosplay.characterName, 
                        updatedCosplay.cosplayer, 
                        updatedCosplay.seriesName,
                        updatedCosplay.started,
                        updatedCosplay.completed,
                        updatedCosplay.description,
                        updatedCosplay.budget,
                        updatedCosplay.cosplayComponents
                    )
                )
        db.run(query)
    }

    def dropUser(id: Long): Future[Int] = {
        val query = 
            cosplaysTable.filter(_.id === id).delete
        db.run(query)
    }
}
