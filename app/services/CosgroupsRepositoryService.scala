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

class CosgroupsRepositoryService @Inject(
    @NamedDatabase("cosgroups") val dbConfigProvider: DatabaseConfigProvider
)(implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
    import profile.api._

    private val cosgroupsTable = TableQuery[CosgroupsTable]
    private val insertProjection =
    cosgroupsTable.map(c =>
        (c.name, c.created, c.archived, c.description, c.admin, c.members, c.nextEvents, c.previousEvents)
    )

    def addNewCosgroup(newCosgroup: CosgroupsRepositoryInsertModel): Future[Int] = {
        db.run(cosgroupsTable.schema.createIfNotExists) // delete on first successful use
        val action =
            insertProjection += (
                newCosgroup.name,
                newCosgroup.created,
                newCosgroup.archived,
                newCosgroup.description,
                newCosgroup.admin,
                newCosgroup.members,
                newCosgroup.nextEvents,
                newCosgroup.previousEvents
            )
        db.run(action)
    }

    def returnAllCosgroup(): Future[Seq[CosgroupsRepositoryModel]] = {
        db.run(cosgroupsTable.result)
    }

    def returnCosgroupsById(id: Long): Future[Option[CosgroupsRepositoryModel]] = {
        db.run(cosgroupsTable.filter(_.id === id).result.headOption)
    }

    def returnCosgroupByAdminId(cosgroupId: Long): Future[Option[CosgroupsRepositoryModel]] = {
        db.run(cosgroupsTable.filter(_.admin === cosgroupId).result.headOption)
    }

    def updateCosgroup(updatedCosgroup: CosgroupsRepositoryModel): Future[Int] = {
        val query = 
            cosgroupsTable.filter(_.id === updatedCosgroup.id)
                .map(c => (c.name, c.created, c.archived, c.description, c.admin, c.members, c.nextEvents, c.previousEvents))
                .update(
                    (
                        updatedCosgroup.name, 
                        updatedCosgroup.created, 
                        updatedCosgroup.archived,
                        updatedCosgroup.description,
                        updatedCosgroup.admin,
                        updatedCosgroup.members,
                        updatedCosgroup.nextEvents,
                        updatedCosgroup.previousEvents
                    )
                )
        db.run(query)
    }

    def dropCosgroup(id: Long): Future[Int] = {
        val query = 
            cosgroupsTable.filter(_.id === id).delete
        db.run(query)
    }
}
