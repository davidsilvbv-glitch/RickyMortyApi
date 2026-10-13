package com.example.rickandmorty.ui.episodes

data class MockCharacter(
    val id: Int,
    val name: String,
    val imageUrl: String
)

data class MockEpisode(
    val id: Int,
    val code: String,
    val name: String,
    val airDate: String,
    val characters: List<MockCharacter>
)

object EpisodeMockData {
    val sampleCharacters = listOf(
        MockCharacter(1, "Rick", "https://rickandmortyapi.com/api/character/avatar/1.jpeg"),
        MockCharacter(2, "Morty", "https://rickandmortyapi.com/api/character/avatar/2.jpeg"),
        MockCharacter(3, "Guest", "https://rickandmortyapi.com/api/character/avatar/180.jpeg"),
        MockCharacter(4, "Summer", "https://rickandmortyapi.com/api/character/avatar/3.jpeg"),
        MockCharacter(5, "Beth", "https://rickandmortyapi.com/api/character/avatar/4.jpeg"),
        MockCharacter(6, "Jerry", "https://rickandmortyapi.com/api/character/avatar/5.jpeg")
    )

    val sampleEpisodes = listOf(
        MockEpisode(
            id = 1,
            code = "S01E01",
            name = "Pilot",
            airDate = "Jan 27, 2022",
            characters = sampleCharacters
        ),
        MockEpisode(
            id = 2,
            code = "S01E02",
            name = "Lawnmower Dog",
            airDate = "Jul 16, 2021",
            characters = sampleCharacters
        ),
        MockEpisode(
            id = 3,
            code = "S01E03",
            name = "Anatomy Park",
            airDate = "Jul 17, 2021",
            characters = sampleCharacters
        ),
        MockEpisode(
            id = 4,
            code = "S01E04",
            name = "M. Night Shaym-Aliens!",
            airDate = "Jul 27, 2022",
            characters = sampleCharacters
        ),
        MockEpisode(
            id = 5,
            code = "S01E05",
            name = "Meeseeks and Destroy",
            airDate = "Jan 17, 2022",
            characters = sampleCharacters
        ),
        MockEpisode(
            id = 6,
            code = "S01E06",
            name = "Rick Potion #9",
            airDate = "Jan 27, 2014",
            characters = sampleCharacters
        ),
        MockEpisode(
            id = 7,
            code = "S01E07",
            name = "Raising Gazorpazorp",
            airDate = "Mar 10, 2014",
            characters = sampleCharacters
        ),
        MockEpisode(
            id = 8,
            code = "S01E08",
            name = "Rixty Minutes",
            airDate = "Mar 17, 2014",
            characters = sampleCharacters
        )
    )
}
