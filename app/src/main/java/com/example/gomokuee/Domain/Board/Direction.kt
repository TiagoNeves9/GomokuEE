package com.example.gomokuee.Domain.Board


/**
 * Direction of a cell movement enum class.
 * Here, "dif" means difference, and "Col" column.
 * By passing a pair of values between -1 and 1, you can get all the possible
 * directions that a cell can move on the Reversi board. Using words like
 * "UP", "DOWN_LEFT", "RIGHT", etc., it's easier to read and understand where
 * the piece will be moving.
 */
enum class Direction(val difRow: Int, val difCol: Int) {
    UP(-1, 0), DOWN(1, 0),
    LEFT(0, -1), RIGHT(0, 1),
    UP_LEFT(-1, -1), UP_RIGHT(-1, 1),
    DOWN_LEFT(1, -1), DOWN_RIGHT(1, 1)
}