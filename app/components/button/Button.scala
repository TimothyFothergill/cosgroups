package components.button

case class Button(
    text: String,
    href: Option[String],
    additionalClasses: Option[String],
    isActive: Boolean = false
)
