package com.hermespiassi.casados.marrecobot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;


class MarrecoBotTest {
    private GameIntel.StepBuilder stepBuilder;
    private List<GameIntel.RoundResult> results;
    private List<TrucoCard> openCards;
    private List<TrucoCard> botCards;
    private TrucoCard vira;

    @Test
    @DisplayName("Should return pica-fumo in first raise if bot has a pica-fumo")
    void shouldReturnPicaFumoInFirstRaiseIfBotHasAPicaFumo() {
        results = List.of();
        botCards = List.of(TrucoCard.of(TWO, HEARTS), TrucoCard.of(FIVE, CLUBS), TrucoCard.of(TWO, DIAMONDS));
        vira = TrucoCard.of(ACE, HEARTS);
        openCards = List.of(vira);
        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(results, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0);

        CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());
        assertThat(cardToPlay.value().getSuit()).isEqualTo(DIAMONDS);
    }

    @Test
    @DisplayName("Should not return pica-fumo if opponent card is manilha")
    void shouldNotReturnPicaFumoIfOpponentCardIsManilha() {
        results = List.of();
        botCards = List.of(
                TrucoCard.of(TWO, HEARTS),
                TrucoCard.of(FIVE, CLUBS),
                TrucoCard.of(TWO, DIAMONDS)
        );
        vira = TrucoCard.of(ACE, HEARTS);
        openCards = List.of(vira, TrucoCard.of(TWO, SPADES));
        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(results, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(TrucoCard.of(TWO, SPADES));

        CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());

        assertThat(cardToPlay.value().getSuit()).isNotEqualTo(DIAMONDS);
    }

    @Test
    @DisplayName("Should return pica-fumo if opponent card is not manilha")
    void shouldReturnPicaFumoIfOpponentCardIsNotManilha() {
        results = List.of();
        botCards = List.of(
                TrucoCard.of(TWO, HEARTS),
                TrucoCard.of(FIVE, CLUBS),
                TrucoCard.of(TWO, DIAMONDS)
        );
        vira = TrucoCard.of(ACE, HEARTS);
        openCards = List.of(vira, TrucoCard.of(THREE, CLUBS));
        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(results, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(TrucoCard.of(THREE, CLUBS));

        CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());

        assertThat(cardToPlay.value().getSuit()).isEqualTo(DIAMONDS);
    }

    @Test
    @DisplayName("Should return manilha if bot have manilha and opponent card is pica-fumo")
    void shouldReturnManilhaIfBotHaveManilhaAndOpponentCardIsPicaFumo() {
        results = List.of();
        botCards = List.of(
                TrucoCard.of(FIVE, HEARTS),
                TrucoCard.of(TWO, HEARTS),
                TrucoCard.of(FOUR, DIAMONDS)
        );
        vira = TrucoCard.of(ACE, HEARTS);
        openCards = List.of(vira, TrucoCard.of(TWO, DIAMONDS));
        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(results, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(TrucoCard.of(TWO, DIAMONDS));

        CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());

        assertThat(cardToPlay.value()).isIn(
                List.of(
                        TrucoCard.of(TWO, SPADES),
                        TrucoCard.of(TWO, HEARTS),
                        TrucoCard.of(TWO, CLUBS)
                )
        );
    }

    @Test
    @DisplayName("Should return less manilha if opponent card is pica-fumo and bot have two manilhas")
    void shouldReturnLessManilhaIfOpponentCardIsPicaFumoAndBotHaveTwoManilhas() {
        results = List.of();
        botCards = List.of(
                TrucoCard.of(TWO, HEARTS),
                TrucoCard.of(FIVE, CLUBS),
                TrucoCard.of(TWO, SPADES)
        );
        vira = TrucoCard.of(ACE, HEARTS);
        openCards = List.of(vira, TrucoCard.of(TWO, DIAMONDS));
        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(results, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(TrucoCard.of(TWO, DIAMONDS));

        CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());

        assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(TWO, SPADES));
    }
}
