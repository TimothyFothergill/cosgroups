package models

case class Event(
    id              : Int,
    name            : String,
    description     : String,
    location        : String
    // expand this for things like: next date, promotion pictures, etc
)
