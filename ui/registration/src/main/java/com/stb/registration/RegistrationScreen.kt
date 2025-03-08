package com.stb.registration

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.stb.ui.registration.R

@Composable
fun RegistrationScreen(navController: NavHostController) {
    Scaffold { paddingValues ->
        RegistrationCard(paddingValues)
    }
}

@Composable
fun RegistrationCard(
    paddingValues: PaddingValues = PaddingValues()
) {
    Card(
        modifier = Modifier.padding(paddingValues)
    ) {
        Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_pizza), contentDescription = "")
    }
}

@Composable
@Preview
private fun RegistrationCardPreview(){
    RegistrationCard()
}