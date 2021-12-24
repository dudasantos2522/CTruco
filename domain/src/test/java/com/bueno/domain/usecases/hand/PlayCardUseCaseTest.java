/*
 *  Copyright (C) 2021 Lucas B. R. de Oliveira - IFSP/SCL
 *  Contact: lucas <dot> oliveira <at> ifsp <dot> edu <dot> br
 *
 *  This file is part of CTruco (Truco game for didactic purpose).
 *
 *  CTruco is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CTruco is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CTruco.  If not, see <https://www.gnu.org/licenses/>
 */

package com.bueno.domain.usecases.hand;

import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.player.util.Player;
import com.bueno.domain.usecases.game.CreateGameUseCase;
import com.bueno.domain.usecases.game.InMemoryGameRepository;
import com.bueno.domain.usecases.game.UnsupportedGameRequestException;
import com.bueno.domain.usecases.hand.PlayCardUseCase.RequestModel;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;
import java.util.logging.LogManager;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayCardUseCaseTest {

    private PlayCardUseCase sut;
    private CreateGameUseCase createGameUseCase;

    @Mock private Player p1;
    @Mock private Player p2;

    private UUID p1Uuid;
    private UUID p2Uuid;

    @BeforeAll
    static void init(){
        LogManager.getLogManager().reset();
    }

    @BeforeEach
    void setUp() {
        p1Uuid = UUID.randomUUID();
        p2Uuid = UUID.randomUUID();

        lenient().when(p1.getUuid()).thenReturn(p1Uuid);
        lenient().when(p1.getUsername()).thenReturn(p1Uuid.toString());

        lenient().when(p2.getUuid()).thenReturn(p2Uuid);
        lenient().when(p2.getUsername()).thenReturn(p2Uuid.toString());

        final InMemoryGameRepository repo = new InMemoryGameRepository();

        createGameUseCase = new CreateGameUseCase(repo);
        createGameUseCase.create(p1, p2);
        sut = new PlayCardUseCase(repo);
    }

    @AfterEach
    void tearDown() {
        sut = null;
        createGameUseCase = null;
        p1Uuid = null;
        p2Uuid = null;
    }

    @Test
    @DisplayName("Should throw if playCard method parameters are null")
    void shouldThrowIfPlayCardMethodParametersAreNull() {
        assertAll(
                () -> assertThrows(UnsupportedGameRequestException.class,
                        () -> sut.playCard(new RequestModel(null, Card.closed()))),
                () -> assertThrows(UnsupportedGameRequestException.class,
                        () -> sut.playCard(new RequestModel(p1.getUuid(), null)))
        );
    }

    @Test
    @DisplayName("Should throw if there is no active game for player UUID")
    void shouldThrowIfThereIsNoActiveGameForPlayerUuid() {
        assertThrows(UnsupportedGameRequestException.class,
                () -> sut.playCard(new RequestModel(UUID.randomUUID(), Card.closed())));
    }

    @Test
    @DisplayName("Should throw if opponent is playing in player turn")
    void shouldThrowIfOpponentIsPlayingInPlayerTurn() {
        assertThrows(UnsupportedGameRequestException.class,
                () -> sut.playCard(new RequestModel(p2Uuid, Card.closed())));
    }

    @Test
    @DisplayName("Should throw if requests action and the game is done")
    void shouldThrowIfRequestsActionAndTheGameIsDone() {
        when(p1.getScore()).thenReturn(12);
        assertThrows(UnsupportedGameRequestException.class,
                () -> sut.playCard(new RequestModel(p1Uuid, Card.closed())));
    }
}