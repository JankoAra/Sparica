package com.example.sparica.ui.composables

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sparica.R
import com.example.sparica.ui.theme.SparicaTheme


data class Message(val author: String, val body: String)

//Modifier upravlja oblikovanjem elemenata(kao CSS)


//LazyColumn i LazyRow renderuju samo vidljive elemente na ekranu
@Composable
fun Conversation(messages: List<Message>) {
    LazyColumn {
        items(messages) { message -> MessageCard(msg = message) }
    }
}

val lista:List<Message> = listOf()

@Preview(showBackground = true)
@Composable
fun PreviewConversation() {
    SparicaTheme {
        Conversation(messages = lista)
    }
}

@Composable
fun MessageCard(msg: Message) {
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = painterResource(R.drawable.janko_profil_glava),
            contentDescription = "Slika glave",
            modifier = Modifier
                // Set image size to 40 dp
                .size(40.dp)
                // Clip image to be shaped as a circle
                .clip(CircleShape)
                .border(2.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )
        // Add a horizontal space between the image and the column
        Spacer(modifier = Modifier.width(8.dp))
        // We keep track if the message is expanded or not in this
        // variable
        var isExpanded by remember { mutableStateOf(false) }
        // surfaceColor will be updated gradually from one color to the other
        val surfaceColor by animateColorAsState(
            if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        )
        Column(Modifier.clickable { isExpanded = !isExpanded }) {
            Text(
                text = msg.author,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall
            )
            // Add a vertical space between the author and message texts
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                shape = MaterialTheme.shapes.medium, shadowElevation = 1.dp,
                // surfaceColor color will be changing gradually from primary to surface
                color = surfaceColor,
                // animateContentSize will change the Surface size gradually
                modifier = Modifier
                    .animateContentSize()
                    .padding(1.dp)
            ) {
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(5.dp),
                    // If the message is expanded, we display all its content
                    // otherwise we only display the first line
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

        }

    }
}

//Preview funkcije ne smeju imati parametre i moraju biti i Composable, radi prikaz neke funkcije bez pokretanja
// u emulatoru ili telefonu
//Mogu da imam vise Preview nad istom funkcijom
@Preview(showBackground = true, name = "Dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, name = "Light mode", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PreviewMessageCard() {
    SparicaTheme {
        Surface {
            MessageCard(Message("Janko", "Cao, ovde covek"))
        }
    }
}