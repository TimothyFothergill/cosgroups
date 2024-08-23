package models

import java.util.Currency
import java.awt.Image

final case class Cosplay(
    characterName: String,
    seriesName: String,
    budget: Currency,
    homemadeElement: HomemadeElement,
    purchasedElement: PurchasedElement,
    referenceImages: Seq[Image],
    inProgressImages: Seq[Image],
    finishedCosplayImages: Seq[Image],
    
)
