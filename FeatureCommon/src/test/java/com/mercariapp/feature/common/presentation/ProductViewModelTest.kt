package com.mercariapp.feature.common.presentation

import com.mercariapp.core.domain.Product
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be false`
import org.amshove.kluent.`should be true`
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class ProductViewModelTest {

    @Nested
    @DisplayName("With a ProductViewModel that is on sale")
    inner class OnSale : ProductViewModelTest(
        Product(
            id = "onSaleProduct id",
            name = "onSaleProduct name",
            status = Product.Status.ON_SALE,
            likeCount = 100,
            commentCount = 200,
            priceInYen = 300,
            photoUrl = "https://dummyimage.com/400x400/000/fff?text=men1"
        )
    ) {
        @Test
        @DisplayName("soldOut is false")
        fun notSoldOut() {
            tested.soldOut.`should be false`()
        }
    }

    @Nested
    @DisplayName("With a ProductViewModel that is sold out")
    inner class SoldOut : ProductViewModelTest(
        Product(
            id = "soldOutProduct id",
            name = "soldOutProduct name",
            status = Product.Status.SOLD_OUT,
            likeCount = 150,
            commentCount = 250,
            priceInYen = 350,
            photoUrl = "https://dummyimage.com/400x400/000/fff?text=men2"
        )
    ) {
        @Test
        @DisplayName("soldOut is true")
        fun soldOut() {
            tested.soldOut.`should be true`()
        }
    }

    abstract inner class ProductViewModelTest(val product: Product) {

        val tested = ProductViewModel(product)

        @Test
        @DisplayName("Name matches given product's name")
        fun nameCorrect() {
            tested.name `should be equal to` product.name
        }

        @Test
        @DisplayName("imageUrl matches given product's photoUrl")
        fun imageUrlCorrect() {
            tested.imageUrl `should be equal to` product.photoUrl
        }

        @Test
        @DisplayName("likeCount matches given product's likeCount")
        fun likeCountCorrect() {
            tested.likeCount `should be equal to` product.likeCount
        }

        @Test
        @DisplayName("commentCount matches given product's commentCount")
        fun commentCountCorrect() {
            tested.commentCount `should be equal to` product.commentCount
        }

        @Test
        @DisplayName("price matches given product's priceInYen")
        fun priceCorrect() {
            tested.price `should be equal to` product.priceInYen
        }
    }
}