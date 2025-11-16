package models

case class Cosplay(
    id: Long,
    characterName: String,
    seriesName: String,
    started: java.time.LocalDate,
    completed: Option[java.time.LocalDate] = None,
    description: Option[String] = None
)
