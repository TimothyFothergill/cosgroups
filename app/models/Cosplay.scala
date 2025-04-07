package models

import java.util.Date
import java.awt.Image

final case class Cosplay(
    characterName: String,
    username: String,
    seriesName: String,
    budget: Float = 0.00,
    startDate: Option[Date],
    endDate: Option[Date] = None,
    icon: Option[Image] = None,
    progress: Float = 0.00,
    homemadeElements: Option[Seq[HomemadeElement]] = None,
    purchasedElements: Option[Seq[PurchasedElement]] = None,
    referenceImages: Option[Seq[Image]] = None,
    inProgressImages: Option[Seq[Image]] = None,
    finishedCosplayImages: Option[Seq[Image]] = None,
)
