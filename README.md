# CS342_WordGuessingGame

A Word Guessing Game Implementing Multi-Threaded Server & Client

Author: *Manh Phan, Lei Chen*

<br/>

<div align="center">
    <img src="https://github.com/Mightymanh/CS342_WordGuessingGame/assets/92337557/e08e5029-3a52-4793-8d9a-171b259ea209" width=300em/>
    <img src="https://github.com/Mightymanh/CS342_WordGuessingGame/assets/92337557/5ce44af2-5d7a-4578-bc78-9576d7717bed" width=300em/>
    <img src="https://github.com/Mightymanh/CS342_WordGuessingGame/assets/92337557/f68c151e-e78b-4f16-baa7-2c26ecb13a51" width=300em/>
</div>


## Description

This project is a simple Guessing Game where a player has to log into the matching port of the server to enter the game. After the connection between client and server is successful, the player will select one from the given three categories and guess the hidden word that relates to the selected category. The server implements a basic multi-threading that allows it to accept multiple connection requests. FXML, JavaFX, and CSS are used for laying out the scenes and the appearance of the game.

## Game rule

Each client that logs into the server must guess **3 different words in 3 different categories to win**. First, the client picks a category. The server will send the client the number of letters in a word from that category to guess. The client gets to guess a total of six letters, one at a time. Correct guesses do not count towards the six guess total. The client will guess a letter and send it to the server. The server will respond with either: the letter is in the word and where that letter is located or the letter is not in the word and how many guesses are left. If the client guesses the word within 6 letter guesses, they can not guess at another word in the same category but must choose from the two remaining. **If they do not guess the word correctly, they are free to choose from any of the three categories for another word. Clients may guess a maximum of three words per category. If they do not make a correct guess within three attempts, the game is over.** The game is won when the client successfully guesses one word in each category. When the game is over, the client can either play again or quit.
