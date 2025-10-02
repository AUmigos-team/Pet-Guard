package br.com.petguard.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.petguard.R

@Composable
fun GuardPetLogo(
    modifier: Modifier = Modifier,
    fontSizeMain: Int = 55,
    fontSizeSub: Int = 40,
    subtitleSize: Int = 16
) {
    val ralewayDots = FontFamily(Font(R.font.raleway_dots_regular))
    val windSong = FontFamily(Font(R.font.windsong_regular))
    val playpenSans = FontFamily(Font(R.font.playpensans_variablefont_wght))

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "GUARD",
                fontSize = fontSizeMain.sp,
                fontFamily = ralewayDots,
                color = Color(0xFF7E8C54)
            )
            Text(
                text = "Pet",
                fontSize = fontSizeSub.sp,
                fontFamily = windSong,
                color = Color(0xFF7E8C54),
                modifier = Modifier.offset(x = 70.dp, y = 25.dp)
            )
        }

        Text(
            text = "Ajude a proteger os animais",
            fontSize = subtitleSize.sp,
            fontWeight = FontWeight.Light,
            fontFamily = playpenSans,
            color = Color(0xFF452001)
        )
    }
}
