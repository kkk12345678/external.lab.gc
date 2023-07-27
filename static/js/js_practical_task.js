'use strict';
/**
 * You must return a date that comes in a predetermined number of seconds after 01.06.2020 00:00:002020
 * @param {number} seconds
 * @returns {Date}
 *
 * @example
 *      31536000 -> 01.06.2021
 *      0 -> 01.06.2020
 *      86400 -> 02.06.2020
 */
const secondsToDate = (seconds) => {
    if (isNaN(seconds)) {
        throw new TypeError('Argument is not a number.');
    }
    if (seconds < 0) {
        return null;
    }
    const date = new Date(2020, 6, 1);
    date.setSeconds(seconds);
    return date;
}

/**
 * You must create a function that returns a base 2 (binary) representation of a base 10 (decimal) string number
 * ! Numbers will always be below 1024 (not including 1024)
 * ! You are not able to use parseInt
 * @param {number} decimal
 * @return {string}
 *
 * @example
 *      5 -> "101"
 *      10 -> "1010"
 */
const toBase2Converter = (decimal) => {
    if (isNaN(decimal)) {
        throw new TypeError('Argument is not a number.');
    }
    if (decimal === 0 || decimal.toString() === "0") {
        return "0";
    }
    let int = Math.abs(Math.floor(decimal));
    let result = "";
    while (int > 0) {
        result = (int % 2) + result;
        int = Math.floor(int / 2);
    }
    return (decimal < 0) ? "-" + result : result;
}

/**
 * You must create a function that takes two strings as arguments and returns the number of times the first string
 * is found in the text.
 * @param {string} substring
 * @param {string} text
 * @return {number}
 *
 * @example
 *      'a', 'test it' -> 0
 *      't', 'test it' -> 2
 *      'T', 'test it' -> 2
 */
const substringOccurrencesCounter = (substring, text) => {
    substring = substring.toLowerCase();
    text = text.toLowerCase();
    let counter = 0;
    for (let i = 0; i <= text.length - substring.length; i++) {
        if (substring === text.substring(i, i + substring.length)) {
            counter++;
        }
    }
    return counter;
}

/**
 * You must create a function that takes a string and returns a string in which each character is repeated once.
 *
 * @param {string} string
 * @return {string}
 *
 * @example
 *      "Hello" -> "HHeelloo"
 *      "Hello world" -> "HHeello  wworrldd" // o, l is repeated more then once. Space was also repeated
 */
const repeatingLitters = (string) => {
    const set = new Set();
    if (string.length === 0) {
        return "";
    }
    let result = "";
    for (let i = 0; i < string.length; i++) {
        const letter = string.charAt(i);
        if (set.has(string.charAt(i))) {
            result += string.charAt(i);
        } else {
            result += letter + letter;
            set.add(letter);
        }
    }
    return result;
}

/**
 * You must write a function redundant that takes in a string str and returns a function that returns str.
 * ! Your function should return a function, not a string.
 *
 * @param {string} str
 * @return {function}
 *
 * @example
 *      const f1 = redundant("apple")
 *      f1() ➞ "apple"
 *
 *      const f2 = redundant("pear")
 *      f2() ➞ "pear"
 *
 *      const f3 = redundant("")
 *      f3() ➞ ""
 */
const redundant = (str) => {
    return () => {return str};
}

const moves = [];

function move(n, from, to, via) {
    if (n === 0) {
        return;
    }
    move(n - 1, from, via, to);
    moves.push([from, to]);
    move(n - 1, via, to, from);
}

/**
 * https://en.wikipedia.org/wiki/Tower_of_Hanoi
 *
 * @param {number} disks
 * @return {array}
 */
const towerHanoi = (disks) => {
    move(disks, 0, 2, 1);
    return moves;
}

/**
 * You must create a function that multiplies two matrices (n x n each).
 *
 * @param {array} matrix1
 * @param {array} matrix2
 * @return {array}
 *
 */
const matrixMultiplication = (matrix1, matrix2) => {
    const r1 = matrix1.length;
    const r2 = matrix2.length;
    if (r1 === 0 || r2 === 0) {
        throw new Error("No matrix specified.")
    }
    const c1 = matrix1[0].length;
    const c2 = matrix2[0].length;
    if (c1 !== r2) {
        throw new Error("No product exists.")
    }
    let result = [r1];
    for (let i = 0; i < r1; i++) {
        result[i] = [c2];
        for (let j = 0; j < c2; j++) {
            let s = 0;
            for (let k = 0; k < c1; k++) {
                s += matrix1[i][k] * matrix2[k][j];
            }
            result[i][j] = s;
        }
    }
    return result;
}

/**
 * Create a gather function that accepts a string argument and returns another function.
 * The function calls should support continued chaining until order is called.
 * order should accept a number as an argument and return another function.
 * The function calls should support continued chaining until get is called.
 * get should return all of the arguments provided to the gather functions as a string in the order specified in the order functions.
 *
 * @param {string} str
 * @return {any}
 *
 * @example
 *      gather("a")("b")("c").order(0)(1)(2).get() ➞ "abc"
 *      gather("a")("b")("c").order(2)(1)(0).get() ➞ "cba"
 *      gather("e")("l")("o")("l")("!")("h").order(5)(0)(1)(3)(2)(4).get()  ➞ "hello"
 */
const gather = (function (str) {
    const array = [];
    const orders = [];

    function order(index) {
        return function g(index, ...args) {
            orders.push(index);
            return (..._args) => {
                return g(...args, ..._args);
            }
        }
    }
    return function f(str, ...args) {
        array.push(str);
        return (..._args) => {
            return f(... args, ..._args);
        }
    }



    /*
    const order = (number)  => {
        orders.push(number);
        return gather;
    }

    const get = () => {
        let result = "";
        for (let i = 0; i < orders.length; i++) {
            result += array[i];
        }
        return result;
    }


     */

})();


module.exports = {secondsToDate, toBase2Converter, substringOccurrencesCounter,
    repeatingLitters, redundant, matrixMultiplication, towerHanoi, gather};