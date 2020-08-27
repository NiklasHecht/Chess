<template>
  <div class="gameBoard">
    <div>
      <div class="moves">
        <div class="moves--title">
          <span class="moves--title-white">White</span>
          <span class="moves--title-black">Black</span>
        </div>
        <div class="log-board">
          <span
              v-for="(move, i) in moves"
              class="single-move"
              :key="i"
          >
            <span class="move-number">{{ i + 1 }}.</span>
            <span
                @click="resetTo(i - 1)"
                class="moveLog"
                title="Revert this move"
            >
              {{ numberToLetter(move[0][1]) }}{{ 8 - move[0][0] }}
              ->
              {{ numberToLetter(move[1][1]) }}{{ 8 - move[1][0] }}
            </span>
            <img
                class="arrow-back"
                title="Revert this move"
                src="https://img.icons8.com/windows/32/000000/long-arrow-left.png"
                @click="resetTo(i - 1)"
                alt="arrow-back"
            />
          </span>
        </div>
      </div>
    </div>
    <div class="main-content">
      <div
          class="board"
          :style="style"
      >
        <div
            v-for="(line, i) in board"
            :key="i + 1"
        >
          <span
              v-for="(tile, j) in line"
              :key="(i + 1) * 8 + j + 1"
              @click="handleClick(i, j)"
              class="tile"
              :id="`tile-${((i + 1) * 7 + j) % 2 === 0}`"
              :class="(((i + 1) * 7 + j) % 2 === 0) ? 'tile--white' : 'tile--black'"
              tabindex="1"
              @keydown.up="handleKey('up')"
              @keydown.left="handleKey('left')"
              @keydown.right="handleKey('right')"
              @keydown.down="handleKey('down')"
              @keydown.enter="handleClick(i, j)"
          >
            <span
                v-if="(selected[0] === i && selected[1] === j)"
                class="focusedTile"
            />
            <span
                v-if="(highlightedTiles[i][j]) && board[i][j] === '  ' && !pawnMovedOver[i][j]"
                class="highlightedTile tile-overlay"
            />
            <span
                v-if="highlightedTiles[i][j] && (board[i][j] !== '  ' || pawnMovedOver[i][j])"
                class="captureAble tile-overlay"
            />
            <span
                v-if="movableWhite[i][j] && showPossibleMoves && playerOne && !highlightedTiles[i][j]"
                class="movable tile-overlay"
            />
            <span
                v-if="movableBlack[i][j] && showPossibleMoves && !playerOne && !highlightedTiles[i][j]"
                class="movable tile-overlay"
            />
            <img
                v-if="tile !== '  '"
                :src="getImageUrl(tile)"
                height="60"
                width="60"
                :alt="tile"
            >
          </span>
        </div>
      </div>
      <div
          class="win-screen"
          v-if="gameEnd"
      />
      <div
          class="win-text"
          v-if="gameEnd"
          @click="restartGame"
      >
        {{ (!playerOne) ? "White" : "Black" }} has won!
      </div>
      <label>
        <input
            class="opacity-slider"
            type="range"
            min="1"
            max="33"
            v-model="opacity"
            @click="updateStyle"
        >
      </label>
    </div>
    <div class="settings-container">
      <label for="showAllPossibleMoves">
        Possible Moves
        <input
            id="showAllPossibleMoves"
            name="show possible moves"
            type="checkbox"
            v-model="showPossibleMoves"
        >
      </label>
    </div>
  </div>
</template>

<script>
export default {
  data: function () {
    return {
      highlightedTiles: [
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false]
      ],
      gotMoved: [
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false]
      ],
      movableWhite: [
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false]
      ],
      movableBlack: [
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false]
      ],
      board: [
        ["ROOK", "KNIGHT", "BISHOP", "QUEEN", "KING", "BISHOP", "KNIGHT", "ROOK"],
        ["PAWN", "PAWN", "PAWN", "PAWN", "PAWN", "PAWN", "PAWN", "PAWN"],
        ["  ", "  ", "  ", "  ", "  ", "  ", "  ", "  "],
        ["  ", "  ", "  ", "  ", "  ", "  ", "  ", "  "],
        ["  ", "  ", "  ", "  ", "  ", "  ", "  ", "  "],
        ["  ", "  ", "  ", "  ", "  ", "  ", "  ", "  "],
        ["pawn", "pawn", "pawn", "pawn", "pawn", "pawn", "pawn", "pawn"],
        ["rook", "knight", "bishop", "queen", "king", "bishop", "knight", "rook"]
      ],
      moves: [],
      selected: [-1, -1],
      playerOne: true,
      isChecked: [false, false],
      gameEnd: false,
      pawnMovedOver: [
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false]
      ],
      focused: [-1, -1],

      boardHistory: [],
      gotMovedHistory: [],
      isCheckedHistory: [],
      pawnMovedOverHistory: [],

      simulatedIsChecked: [false, false],
      simulatedBoard: [
        ["ROOK", "KNIGHT", "BISHOP", "QUEEN", "KING", "BISHOP", "KNIGHT", "ROOK"],
        ["PAWN", "PAWN", "PAWN", "PAWN", "PAWN", "PAWN", "PAWN", "PAWN"],
        ["  ", "  ", "  ", "  ", "  ", "  ", "  ", "  "],
        ["  ", "  ", "  ", "  ", "  ", "  ", "  ", "  "],
        ["  ", "  ", "  ", "  ", "  ", "  ", "  ", "  "],
        ["  ", "  ", "  ", "  ", "  ", "  ", "  ", "  "],
        ["pawn", "pawn", "pawn", "pawn", "pawn", "pawn", "pawn", "pawn"],
        ["rook", "knight", "bishop", "queen", "king", "bishop", "knight", "rook"]
      ],
      simulatedMoveableWhite: [
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false]
      ],
      simulatedMoveableBlack: [
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false]
      ],

      opacity: 1,
      style: `opacity: ${this.opacity * 3}%;`,
      showPossibleMoves: false,
    }
  },
  methods: {
    restartGame() {
      this.playerOne = true;
      this.gotMoved = [
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false]
      ];
      this.movableWhite = [
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false]
      ];
      this.movableBlack = [
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false]
      ];
      this.highlightedTiles = [
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false]
      ];
      this.moves = [];
      this.boardHistory = [];
      this.isCheckedHistory = [];
      this.selected = [-1, -1];
      this.playerOne = true;
      this.gameEnd = false;
      this.isChecked = [false, false];
      this.board = [
        ["ROOK", "KNIGHT", "BISHOP", "QUEEN", "KING", "BISHOP", "KNIGHT", "ROOK"],
        ["PAWN", "PAWN", "PAWN", "PAWN", "PAWN", "PAWN", "PAWN", "PAWN"],
        ["  ", "  ", "  ", "  ", "  ", "  ", "  ", "  "],
        ["  ", "  ", "  ", "  ", "  ", "  ", "  ", "  "],
        ["  ", "  ", "  ", "  ", "  ", "  ", "  ", "  "],
        ["  ", "  ", "  ", "  ", "  ", "  ", "  ", "  "],
        ["pawn", "pawn", "pawn", "pawn", "pawn", "pawn", "pawn", "pawn"],
        ["rook", "knight", "bishop", "queen", "king", "bishop", "knight", "rook"]
      ];
      this.pawnMovedOver = [
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false]
      ];
    },

    handleKey(str) {
      switch (str) {
        case "up":
          if(this.focused[0] !== 0) this.focused[0]--;
          break;
        case "right":
          if(this.focused[1] !== 7) this.focused[1]++;
          break;
        case "left":
          if(this.focused[1] !== 0) this.focused[1]--;
          break;
        case "down":
          if(this.focused[0] !== 7) this.focused[0]++;
          break;
      }
      this.document.focus(this.document.getElementById(`tile-${(this.focused[0] + 1) * 7 + this.focused[1]}`));
    },

    handleClick(i, j) {
      if (!this.gameEnd) {
        if (this.selected[0] === i && this.selected[1] === j) {
          this.deselectAll();
        } else if (this.isValidMove(i, j)) {
          this.move(i, j);
          this.deselectAll();
          if (this.isCheckMate()) {
            this.gameEnd = true;
          }
        } else if (this.board[i][j] !== "  " && this.playerOne && this.isLowerCase(this.board[i][j])
            || !this.playerOne && this.isUpperCase(this.board[i][j])) {
          this.selected = [i, j];
          this.highlight(this.getMovesFor(i, j, this.board));
          this.selected = [i, j];
          this.highlight(this.getMovesFor(i, j, this.board));
        } else if (this.selected[0] !== -1 && !this.isValidMove(i, j) || this.selected[0] === i
            && this.selected[1] === j) {
          this.deselectAll();
        }
      }
    },

    getImageUrl(tile) {
      return require(`../assets/figures/${this.imageColor(tile)}/${tile.toLowerCase()}.png`);
    },

    imageColor(figure) {
      if (this.isUpperCase(figure)) {
        return "black"
      }
      return "white"
    },

    updateStyle() {
      this.style = `opacity: ${this.opacity * 3}%;`;
    },

    resetTo(n) {
      if (this.boardHistory[n + 1]) {
        this.board = this.boardHistory[n + 1];
        this.gotMoved = this.gotMovedHistory[n + 1];
        this.isChecked = this.isCheckedHistory[n + 1];
        this.pawnMovedOver = this.pawnMovedOverHistory[n + 1];
        this.playerOne = n % 2 !== 0;
        for (let i = this.boardHistory.length - 1; i > n; i--) {
          this.boardHistory.pop();
          this.gotMovedHistory.pop();
          this.isCheckedHistory.pop();
          this.pawnMovedOverHistory.pop();
          this.moves.pop();
        }
        this.deselectAll();
        this.gameEnd = false;
      }
    },

    setAllSimulatedPossibleMoves() {
      this.simulatedMoveableBlack = [
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false]
      ];
      this.simulatedMoveableWhite = [
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false]
      ];
      for (let i = 0; i < this.simulatedBoard.length; i++) {
        for (let j = 0; j < this.simulatedBoard[i].length; j++) {
          let figure = this.simulatedBoard[i][j];
          if (figure !== "  ") {
            let moves = this.getMovesFor(i, j, this.simulatedBoard, true);
            if (this.isUpperCase(figure)) {
              for (let n = 0; n < moves.length; n++) {
                if (!this.isUpperCase(this.simulatedBoard[moves[n][0]][moves[n][1]])) {
                  this.simulatedMoveableBlack[moves[n][0]][moves[n][1]] = true;
                }
              }
            } else {
              for (let n = 0; n < moves.length; n++) {
                if (!this.isLowerCase(this.simulatedBoard[moves[n][0]][moves[n][1]])) {
                  this.simulatedMoveableWhite[moves[n][0]][moves[n][1]] = true;
                }
              }
            }
          }
        }
      }
    },

    setAllPossibleMoves() {
      this.movableBlack = [
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false]
      ];
      this.movableWhite = [
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false]
      ];
      for (let i = 0; i < this.board.length; i++) {
        for (let j = 0; j < this.board[i].length; j++) {
          let figure = this.board[i][j];
          if (figure !== "  ") {
            let moves = this.getMovesFor(i, j, this.board);
            if (this.isUpperCase(figure)) {
              for (let n = 0; n < moves.length; n++) {
                if (!this.isUpperCase(this.board[moves[n][0]][moves[n][1]])) {
                  this.movableBlack[moves[n][0]][moves[n][1]] = true;
                }
              }
            } else {
              for (let n = 0; n < moves.length; n++) {
                if (!this.isLowerCase(this.board[moves[n][0]][moves[n][1]])) {
                  this.movableWhite[moves[n][0]][moves[n][1]] = true;
                }
              }
            }
          }
        }
      }
    },

    deselectAll() {
      this.deselectAllHighlighted();
      this.selected = [-1, -1];
    },

    addBoardToHistory() {
      this.boardHistory.push(this.copyBoard(this.board));
      this.gotMovedHistory.push(this.copyBoard(this.gotMoved));
      this.isCheckedHistory.push(this.copyBoard(this.isChecked));
      this.pawnMovedOverHistory.push(this.copyBoard(this.pawnMovedOver));
    },

    move(i, j) {
      this.addBoardToHistory();
      let oldI = this.selected[0];
      let oldJ = this.selected[1];
      if (this.board[oldI][oldJ].toLowerCase() === "pawn") {
        if (this.pawnMovedOver[i][j]) {
          this.board[oldI][j] = "  ";
        }
        this.pawnMovedOver = [
          [false, false, false, false, false, false, false, false],
          [false, false, false, false, false, false, false, false],
          [false, false, false, false, false, false, false, false],
          [false, false, false, false, false, false, false, false],
          [false, false, false, false, false, false, false, false],
          [false, false, false, false, false, false, false, false],
          [false, false, false, false, false, false, false, false],
          [false, false, false, false, false, false, false, false]
        ];
        if (Math.abs(oldI - i) === 2) {
          this.pawnMovedOver[oldI - (oldI - i) / 2][j] = true;
        }
      }
      if (this.board[oldI][oldJ].toLowerCase() === "king" && Math.abs(oldJ - j) > 1) {
        if (oldJ < j) {
          this.board[i][j + 1] = "  ";
          if (this.playerOne) {
            this.board[i][j - 1] = "rook";
          } else {
            this.board[i][j - 1] = "ROOK";
          }
        } else {
          this.board[i][j - 2] = "  ";
          if (this.playerOne) {
            this.board[i][j + 1] = "rook";
          } else {
            this.board[i][j + 1] = "ROOK";
          }
        }
      }
      if (this.board[oldI][oldJ] === "PAWN" && !this.playerOne && i === 7) {
        this.board[i][j] = "QUEEN";
      } else if (this.board[oldI][oldJ] === "pawn" && this.playerOne && i === 0) {
        this.board[i][j] = "queen";
      } else {
        this.board[i][j] = this.board[oldI][oldJ];
      }
      this.board[oldI][oldJ] = "  ";
      this.moves.push([[oldI, oldJ], [i, j]]);

      this.setIsChecked();
      if (this.playerOne) {
        if (this.isChecked[0]) {
          this.resetTo(this.moves.length - 2);
          return
        }
      } else {
        if (this.isChecked[1]) {
          this.resetTo(this.moves.length - 2);
          return
        }
      }

      this.playerOne = !this.playerOne;
      this.gotMoved[oldI][oldJ] = true;
    },

    isCheckMate() {
      this.setAllPossibleMoves();
      let moves = [];
      if (this.playerOne) {
        moves = this.getAllCurrentlyPossibleMovesWhite();
      } else {
        moves = this.getAllCurrentlyPossibleMovesBlack();
      }
      return moves.length === 0;
    },

    numberToLetter(n) {
      switch (n) {
        case 0:
          return "A"
        case 1:
          return "B"
        case 2:
          return "C"
        case 3:
          return "D"
        case 4:
          return "E"
        case 5:
          return "F"
        case 6:
          return "G"
        case 7:
          return "H"
        default:
          return "Error"
      }
    },

    isValidMove(i, j) {
      return this.highlightedTiles[i][j];
    },

    deselectAllHighlighted() {
      this.highlightedTiles = [
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false]
      ];
    },

    resetMoveable() {
      this.movableWhite = [
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false]
      ];
    },

    isUpperCase(str) {
      return str.toLowerCase() !== str;
    },

    isLowerCase(str) {
      return str.toUpperCase() !== str;
    },

    highlight(arr) {
      this.deselectAllHighlighted();
      for (let i = 0; i < arr.length; i++) {
        this.highlightedTiles[arr[i][0]][arr[i][1]] = true;
      }
    },

    getAllCurrentlyPossibleMovesWhite() {
      let result = [];
      for (let i = 0; i < this.movableWhite.length; i++) {
        for (let j = 0; j < this.movableWhite[i].length; j++) {
          if (this.movableWhite[i][j]) {
            result.push([i, j]);
          }
        }
      }
      return result;
    },

    getAllCurrentlyPossibleMovesBlack() {
      let result = [];
      for (let i = 0; i < this.movableBlack.length; i++) {
        for (let j = 0; j < this.movableBlack[i].length; j++) {
          if (this.movableBlack[i][j]) {
            result.push([i, j]);
          }
        }
      }
      return result;
    },

    setIsChecked() {
      this.setAllPossibleMoves();

      for (let i = 0; i < this.board.length; i++) {
        for (let j = 0; j < this.board[i].length; j++) {
          if (this.board[i][j] === "king") {
            this.isChecked[0] = this.movableBlack[i][j];
          }
        }
      }
      for (let i = 0; i < this.board.length; i++) {
        for (let j = 0; j < this.board[i].length; j++) {
          if (this.board[i][j] === "KING") {
            this.isChecked[1] = this.movableWhite[i][j];
          }
        }
      }
    },

    setIsSimulatedChecked() {
      this.setAllSimulatedPossibleMoves();

      for (let i = 0; i < this.simulatedBoard.length; i++) {
        for (let j = 0; j < this.simulatedBoard[i].length; j++) {
          if (this.simulatedBoard[i][j] === "king") {
            this.simulatedIsChecked[0] = this.simulatedMoveableBlack[i][j];
          }
        }
      }
      for (let i = 0; i < this.simulatedBoard.length; i++) {
        for (let j = 0; j < this.simulatedBoard[i].length; j++) {
          if (this.simulatedBoard[i][j] === "KING") {
            this.simulatedIsChecked[1] = this.simulatedMoveableWhite[i][j];
          }
        }
      }
    },

    getMovesFor(i, j, board, simulated = false) {
      let isPlayerOne = this.isLowerCase(board[i][j]);
      let figure = board[i][j].toLowerCase();
      let possibleMoves = [];
      switch (figure) {
        case "rook":
          for (let j2 = j + 1; j2 < 8; j2++) { //horizontal positive
            let figure2 = board[i][j2];
            if (figure2 !== "  ") {
              if ((isPlayerOne && this.isUpperCase(figure2)) || (!isPlayerOne
                  && this.isLowerCase(figure2))) {
                possibleMoves.push([i, j2]);
              }
              break;
            } else {
              possibleMoves.push([i, j2]);
            }
          }
          for (let j2 = j - 1; j2 >= 0; j2--) { //horizontal negative
            let figure2 = board[i][j2];
            if (figure2 !== "  ") {
              if ((isPlayerOne && this.isUpperCase(figure2)) || (!isPlayerOne
                  && this.isLowerCase(figure2))) {
                possibleMoves.push([i, j2]);
              }
              break;
            } else {
              possibleMoves.push([i, j2]);
            }
          }
          for (let i2 = i + 1; i2 < 8; i2++) { //vertical positive
            let figure2 = board[i2][j];
            if (figure2 !== "  ") {
              if ((isPlayerOne && this.isUpperCase(figure2)) || (!isPlayerOne
                  && this.isLowerCase(figure2))) {
                possibleMoves.push([i2, j]);
              }
              break;
            } else {
              possibleMoves.push([i2, j]);
            }
          }
          for (let i2 = i - 1; i2 >= 0; i2--) { //vertical negative
            let figure2 = board[i2][j];
            if (figure2 !== "  ") {
              if ((isPlayerOne && this.isUpperCase(figure2)) || (!isPlayerOne
                  && this.isLowerCase(figure2))) {
                possibleMoves.push([i2, j]);
              }
              break;
            } else {
              possibleMoves.push([i2, j]);
            }
          }
          break;
        case "knight":
          if (i + 2 < 8 && j + 1 < 8 && (board[i + 2][j + 1] === "  " || isPlayerOne
              && this.isUpperCase(board[i + 2][j + 1]) || !isPlayerOne && this.isLowerCase(
                  board[i + 2][j + 1]))) {
            possibleMoves.push([i + 2, j + 1]);
          }
          if (i - 2 >= 0 && j + 1 < 8 && (board[i - 2][j + 1] === "  " || isPlayerOne
              && this.isUpperCase(board[i - 2][j + 1]) || !isPlayerOne && this.isLowerCase(
                  board[i - 2][j + 1]))) {
            possibleMoves.push([i - 2, j + 1]);
          }
          if (i + 2 < 8 && j - 1 >= 0 && (board[i + 2][j - 1] === "  " || isPlayerOne
              && this.isUpperCase(board[i + 2][j - 1]) || !isPlayerOne && this.isLowerCase(
                  board[i + 2][j - 1]))) {
            possibleMoves.push([i + 2, j - 1]);
          }
          if (i - 2 >= 0 && j - 1 >= 0 && (board[i - 2][j - 1] === "  " || isPlayerOne
              && this.isUpperCase(board[i - 2][j - 1]) || !isPlayerOne && this.isLowerCase(
                  board[i - 2][j - 1]))) {
            possibleMoves.push([i - 2, j - 1]);
          }
          if (i + 1 < 8 && j + 2 < 8 && (board[i + 1][j + 2] === "  " || isPlayerOne
              && this.isUpperCase(board[i + 1][j + 2]) || !isPlayerOne && this.isLowerCase(
                  board[i + 1][j + 2]))) {
            possibleMoves.push([i + 1, j + 2]);
          }
          if (i - 1 >= 0 && j + 2 < 8 && (board[i - 1][j + 2] === "  " || isPlayerOne
              && this.isUpperCase(board[i - 1][j + 2]) || !isPlayerOne && this.isLowerCase(
                  board[i - 1][j + 2]))) {
            possibleMoves.push([i - 1, j + 2]);
          }
          if (i + 1 < 8 && j - 2 >= 0 && (board[i + 1][j - 2] === "  " || isPlayerOne
              && this.isUpperCase(board[i + 1][j - 2]) || !isPlayerOne && this.isLowerCase(
                  board[i + 1][j - 2]))) {
            possibleMoves.push([i + 1, j - 2]);
          }
          if (i - 1 >= 0 && j - 2 >= 0 && (board[i - 1][j - 2] === "  " || isPlayerOne
              && this.isUpperCase(board[i - 1][j - 2]) || !isPlayerOne && this.isLowerCase(
                  board[i - 1][j - 2]))) {
            possibleMoves.push([i - 1, j - 2]);
          }
          break;
        case "bishop":
          for (let i2 = i + 1; i2 < 8 && j + (i2 - i) < 8; i2++) { //positive positive
            let figure2 = board[i2][j + (i2 - i)];
            if (figure2 !== "  ") {
              if ((isPlayerOne && this.isUpperCase(figure2)) || (!isPlayerOne
                  && this.isLowerCase(figure2))) {
                possibleMoves.push([i2, j + (i2 - i)]);
              }
              break;
            } else {
              possibleMoves.push([i2, j + (i2 - i)]);
            }
          }
          for (let i2 = i + 1; i2 < 8 && j - (i2 - i) >= 0; i2++) { //positive negative
            let figure2 = board[i2][j - (i2 - i)];
            if (figure2 !== "  ") {
              if ((isPlayerOne && this.isUpperCase(figure2)) || (!isPlayerOne
                  && this.isLowerCase(figure2))) {
                possibleMoves.push([i2, j - (i2 - i)]);
              }
              break;
            } else {
              possibleMoves.push([i2, j - (i2 - i)]);
            }
          }
          for (let i2 = i - 1; i2 >= 0 && j + (i - i2) < 8; i2--) { //negative positive
            let figure2 = board[i2][j + (i - i2)];
            if (figure2 !== "  ") {
              if ((isPlayerOne && this.isUpperCase(figure2)) || (!isPlayerOne
                  && this.isLowerCase(figure2))) {
                possibleMoves.push([i2, j + (i - i2)]);
              }
              break;
            } else {
              possibleMoves.push([i2, j + (i - i2)]);
            }
          }
          for (let i2 = i - 1; i2 >= 0 && j - (i - i2) >= 0; i2--) { //negative negative
            let figure2 = board[i2][j - (i - i2)];
            if (figure2 !== "  ") {
              if ((isPlayerOne && this.isUpperCase(figure2)) || (!isPlayerOne
                  && this.isLowerCase(figure2))) {
                possibleMoves.push([i2, j - (i - i2)]);
              }
              break;
            } else {
              possibleMoves.push([i2, j - (i - i2)]);
            }
          }
          break;
        case "queen":
          for (let i2 = i + 1; i2 < 8 && j + (i2 - i) < 8; i2++) { //positive positive
            let figure2 = board[i2][j + (i2 - i)];
            if (figure2 !== "  ") {
              if ((isPlayerOne && this.isUpperCase(figure2)) || (!isPlayerOne
                  && this.isLowerCase(figure2))) {
                possibleMoves.push([i2, j + (i2 - i)]);
              }
              break;
            } else {
              possibleMoves.push([i2, j + (i2 - i)]);
            }
          }
          for (let i2 = i + 1; i2 < 8 && j - (i2 - i) >= 0; i2++) { //positive negative
            let figure2 = board[i2][j - (i2 - i)];
            if (figure2 !== "  ") {
              if ((isPlayerOne && this.isUpperCase(figure2)) || (!isPlayerOne
                  && this.isLowerCase(figure2))) {
                possibleMoves.push([i2, j - (i2 - i)]);
              }
              break;
            } else {
              possibleMoves.push([i2, j - (i2 - i)]);
            }
          }
          for (let i2 = i - 1; i2 >= 0 && j + (i - i2) < 8; i2--) { //negative positive
            let figure2 = board[i2][j + (i - i2)];
            if (figure2 !== "  ") {
              if ((isPlayerOne && this.isUpperCase(figure2)) || (!isPlayerOne
                  && this.isLowerCase(figure2))) {
                possibleMoves.push([i2, j + (i - i2)]);
              }
              break;
            } else {
              possibleMoves.push([i2, j + (i - i2)]);
            }
          }
          for (let i2 = i - 1; i2 >= 0 && j - (i - i2) >= 0; i2--) { //negative negative
            let figure2 = board[i2][j - (i - i2)];
            if (figure2 !== "  ") {
              if ((isPlayerOne && this.isUpperCase(figure2)) || (!isPlayerOne
                  && this.isLowerCase(figure2))) {
                possibleMoves.push([i2, j - (i - i2)]);
              }
              break;
            } else {
              possibleMoves.push([i2, j - (i - i2)]);
            }
          }
          for (let j2 = j + 1; j2 < 8; j2++) { //horizontal positive
            let figure2 = board[i][j2];
            if (figure2 !== "  ") {
              if ((isPlayerOne && this.isUpperCase(figure2)) || (!isPlayerOne
                  && this.isLowerCase(figure2))) {
                possibleMoves.push([i, j2]);
              }
              break;
            } else {
              possibleMoves.push([i, j2]);
            }
          }
          for (let j2 = j - 1; j2 >= 0; j2--) { //horizontal negative
            let figure2 = board[i][j2];
            if (figure2 !== "  ") {
              if ((isPlayerOne && this.isUpperCase(figure2)) || (!isPlayerOne
                  && this.isLowerCase(figure2))) {
                possibleMoves.push([i, j2]);
              }
              break;
            } else {
              possibleMoves.push([i, j2]);
            }
          }
          for (let i2 = i + 1; i2 < 8; i2++) { //vertical positive
            let figure2 = board[i2][j];
            if (figure2 !== "  ") {
              if ((isPlayerOne && this.isUpperCase(figure2)) || (!isPlayerOne
                  && this.isLowerCase(figure2))) {
                possibleMoves.push([i2, j]);
              }
              break;
            } else {
              possibleMoves.push([i2, j]);
            }
          }
          for (let i2 = i - 1; i2 >= 0; i2--) { //vertical negative
            let figure2 = board[i2][j];
            if (figure2 !== "  ") {
              if ((isPlayerOne && this.isUpperCase(figure2)) || (!isPlayerOne
                  && this.isLowerCase(figure2))) {
                possibleMoves.push([i2, j]);
              }
              break;
            } else {
              possibleMoves.push([i2, j]);
            }
          }
          break;
        case "king":
          if (i + 1 < 8 && (board[i + 1][j] === "  " || isPlayerOne && this.isUpperCase(
              board[i + 1][j]) || !isPlayerOne && this.isLowerCase(
              board[i + 1][j]))) {
            possibleMoves.push([i + 1, j]);
          }
          if (i - 1 >= 0 && (board[i - 1][j] === "  " || isPlayerOne
              && this.isUpperCase(board[i - 1][j]) || !isPlayerOne && this.isLowerCase(
                  board[i - 1][j]))) {
            possibleMoves.push([i - 1, j]);
          }
          if (j + 1 < 8 && (board[i][j + 1] === "  " || isPlayerOne && this.isUpperCase(
              board[i][j + 1]) || !isPlayerOne && this.isLowerCase(
              board[i][j + 1]))) {
            possibleMoves.push([i, j + 1]);
          }
          if (j - 1 >= 0 && (board[i][j - 1] === "  " || isPlayerOne
              && this.isUpperCase(board[i][j - 1]) || !isPlayerOne && this.isLowerCase(
                  board[i][j - 1]))) {
            possibleMoves.push([i, j - 1]);
          }
          if (i + 1 < 8 && j + 1 < 8 && (board[i + 1][j + 1] === "  " || isPlayerOne
              && this.isUpperCase(board[i + 1][j + 1]) || !isPlayerOne && this.isLowerCase(
                  board[i + 1][j + 1]))) {
            possibleMoves.push([i + 1, j + 1]);
          }
          if (i - 1 >= 0 && j - 1 >= 0 && (board[i - 1][j - 1] === "  " || isPlayerOne
              && this.isUpperCase(board[i - 1][j - 1]) || !isPlayerOne && this.isLowerCase(
                  board[i - 1][j - 1]))) {
            possibleMoves.push([i - 1, j - 1]);
          }
          if (i + 1 < 8 && j - 1 >= 0 && (board[i + 1][j - 1] === "  " || isPlayerOne
              && this.isUpperCase(board[i + 1][j - 1]) || !isPlayerOne && this.isLowerCase(
                  board[i + 1][j - 1]))) {
            possibleMoves.push([i + 1, j - 1]);
          }
          if (i - 1 >= 0 && j + 1 < 8 && (board[i - 1][j + 1] === "  " || isPlayerOne
              && this.isUpperCase(board[i - 1][j + 1]) || !isPlayerOne && this.isLowerCase(
                  board[i - 1][j + 1]))) {
            possibleMoves.push([i - 1, j + 1]);
          }
          if (!this.gotMoved[i][j] && !this.isChecked[0] && !this.isChecked[1] && (i === 7 || i
              === 0)) {
            if (!this.gotMoved[i][7] && board[i][6] === "  " && board[i][5] ===
                "  ") {
              possibleMoves.push([i, j + 2]);
            }
            if (!this.gotMoved[i][0] && board[i][3] === "  " && board[i][2] === "  "
                && board[i][1] === "  ") {
              possibleMoves.push([i, j - 2]);
            }
          }
          break;
        case "pawn":
          if (isPlayerOne) {
            if (i - 1 >= 0) {
              if (board[i - 1][j] === "  ") {
                possibleMoves.push([i - 1, j]);
                if (i === 6 && (board[i - 2][j] === "  ")) {
                  possibleMoves.push([i - 2, j]);
                }
              }
              if (j + 1 < 8 && (this.isUpperCase(board[i - 1][j + 1]) && board[i - 1][j
              + 1] !== "  ") || this.pawnMovedOver[i - 1][j + 1]) {
                possibleMoves.push([i - 1, j + 1]);
              }
              if (j - 1 >= 0 && (this.isUpperCase(board[i - 1][j - 1]) && board[i - 1][j
              - 1] !== "  ") || this.pawnMovedOver[i - 1][j - 1]) {
                possibleMoves.push([i - 1, j - 1]);
              }
            }
          } else {
            if (i + 1 < 8) {
              if (board[i + 1][j] === "  ") {
                possibleMoves.push([i + 1, j]);
                if (i === 1 && (board[i + 2][j] === "  ")) {
                  possibleMoves.push([i + 2, j]);
                }
              }
              if (j + 1 < 8 && (this.isLowerCase(board[i + 1][j + 1]) && board[i + 1][j
              + 1] !== "  ") || this.pawnMovedOver[i + 1][j + 1]) {
                possibleMoves.push([i + 1, j + 1]);
              }
              if (j - 1 >= 0 && (this.isLowerCase(board[i + 1][j - 1]) && board[i + 1][j
              - 1] !== "  ") || this.pawnMovedOver[i + 1][j - 1]) {
                possibleMoves.push([i + 1, j - 1]);
              }
            }

          }
          break;
      }
      if (!simulated) {
        return this.filterMoves(possibleMoves, i, j);
      } else {
        return possibleMoves;
      }
    },

    filterMoves(moves, nextI, nextJ) {
      let validMoves = [];
      for (let i = 0; i < moves.length; i++) {
        this.simulateMove(moves[i][0], moves[i][1], nextI, nextJ);
        if (this.playerOne) {
          if (!this.simulatedIsChecked[0]) {
            validMoves.push(moves[i]);
          }
        } else {
          if (!this.simulatedIsChecked[1]) {
            validMoves.push(moves[i]);
          }
        }
      }
      return validMoves;
    },

    copyBoard(board) {
      let arr = [];
      let line = [];
      for (let i = 0; i < board.length; i++) {
        for (let j = 0; j < board[i].length; j++) {
          line.push(board[i][j]);
        }
        arr.push(line);
        line = [];
      }
      return arr;
    },

    simulateMove(i, j, oldI, oldJ) {
      this.simulatedBoard = this.copyBoard(this.board);
      if (this.simulatedBoard[oldI][oldJ] === "PAWN" && !this.playerOne && i === 7) {
        this.simulatedBoard[i][j] = "QUEEN";
      } else if (this.simulatedBoard[oldI][oldJ] === "pawn" && this.playerOne && i === 0) {
        this.simulatedBoard[i][j] = "queen";
      } else {
        this.simulatedBoard[i][j] = this.simulatedBoard[oldI][oldJ];
      }
      this.simulatedBoard[oldI][oldJ] = "  ";
      this.setIsSimulatedChecked();
    },
  }
}
</script>

<style>
.moves--title > * {
  width: 100%;
  height: 100%;
  text-align: center;
}

.moves--title-white {
  color: black;
  height: 30px;
  align-items: center;
  justify-content: center;
  display: flex;
  background-color: white;
}

.arrow-back {
  height: 23px;
  transform: translateY(6px);
  cursor: pointer;
}

.log-board {
  display: grid;
  grid-template-columns: 1fr 1fr;
}

.single-move:nth-child(2n) {
  border-left: solid 1px black;
}

.moves--title-black {
  color: white;
  height: 30px;
  align-items: center;
  justify-content: center;
  display: flex;
  background-color: black;
}

.moves--title {
  transform: translateY(-30px);
  width: 255px;
  display: flex;
  position: fixed;
  justify-content: space-evenly;
  border-bottom: solid 1px #888;
}

.moves {
  width: 255px;
  height: 784px;
  background-color: white;
  overflow: auto;
}

.moves::-webkit-scrollbar {
  width: 0;
}

.gameBoard {
  display: grid;
  grid-template: auto / 1fr 820px 1fr;
  grid-gap: 30px;
}

.main-content {
  position: relative;
}

.win-screen {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  background-color: #333;
  opacity: .9;
  z-index: 1000;
}

.win-text {
  position: absolute;
  width: 100%;
  height: 100%;
  font-size: 60px;
  top: 0;
  left: 0;
  justify-content: center;
  align-items: center;
  display: flex;
  opacity: 1;
  z-index: 1001;
  cursor: default;
}

.opacity-slider {
  position: absolute;
  right: 0;
  top: 100%;
}

.tile-overlay {
  width: 100%;
  height: 100%;
  opacity: 40%;
  position: absolute;
  top: 0;
  left: 0;
}

.captureAble {
  background-color: red;
  outline: solid 1px red;
}

.highlightedTile {
  background-color: yellow;
  outline: solid 1px yellow;
}

.movable {
  background-color: green;
  outline: solid 1px green;
}

.tile {
  position: relative;
  cursor: pointer;
  user-select: none;
}

.tile--white {
  background-color: white;
}

.tile--black {
  background-color: cornflowerblue;
}

.single-move {
  display: flex;
  justify-content: space-between;
  align-items: baseline;
}

.single-move {
  border-bottom: solid 1px black;
}

.move-number {
  font-size: 20px;
  font-weight: 600;
}

.moveLog {
  cursor: pointer;
}

.focusedTile {
  height: 80px;
  width: 80px;
  position: absolute;
  top: 8px;
  left: 8px;
  border-radius: 50%;
  border: dashed 2px black;
}

.board {
  opacity: 3%;
  border: solid 10px black;
  width: 800px;
}

.board > * {
  width: 800px;
  height: 100px;
  display: flex;
  justify-content: space-evenly;
  align-items: center;
}

.board > * > * {
  width: 98px;
  height: 98px;
  display: flex;
  justify-content: center;
  align-items: center;
  border: solid 1px transparent;
}

.board > * > *:hover, .board > * > *:focus {
  border: solid 1px black;
  outline: none;
}

.board > * > *:focus:after {
  width: 100%;
  height: 100%;
  background-color: dodgerblue;
  opacity: 40%;
}
</style>
