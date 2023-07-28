const {secondsToDate, toBase2Converter, substringOccurrencesCounter,
   repeatingLitters, redundant, matrixMultiplication, towerHanoi, gather}
    = require("./js_practical_task");

describe("SecondsToDate() test.", () => {
   test("Illegal argument - negative.", () => {
      expect(secondsToDate(Number(-31536000))).toStrictEqual(null);
   });

   test("Illegal argument - double.", () => {
      expect(secondsToDate(Number(86400.201))).toStrictEqual(new Date(2020, 6, 2));
   });

   test("Illegal argument - valid string.", () => {
      expect(secondsToDate(Number("86400.201"))).toStrictEqual(new Date(2020, 6, 2));
   });

   test("Illegal argument - invalid string.", () => {
      expect(() => secondsToDate(Number("wefted"))).toThrow(TypeError);
      expect(() => secondsToDate(Number("wefted"))).toThrow('Argument is not a number.');
   });

   test("Returns 01.06.2021 for 31536000.", () => {
      expect(secondsToDate(Number(31536000))).toStrictEqual(new Date(2021, 6, 1));
   });

   test("Returns 01.06.2020 for 0.", () => {
      expect(secondsToDate(Number(0))).toStrictEqual(new Date(2020, 6, 1));
   });

   test("Returns 01.06.2020 for 86400.", () => {
      expect(secondsToDate(Number(86400))).toStrictEqual(new Date(2020, 6, 2));
   });
});

describe("toBase2Converter() test.", () => {
   test("Returns '0' for 0.", () => {
      expect(toBase2Converter(0)).toStrictEqual("0");
   });

   test("Returns '0' for '0'.", () => {
      expect(toBase2Converter('0')).toStrictEqual("0");
   });

   test("Returns '101' for 5.", () => {
      expect(toBase2Converter(5)).toStrictEqual("101");
   });

   test("Returns '-101' for -5.", () => {
      expect(toBase2Converter(-5)).toStrictEqual("-101");
   });

   test("Returns '101' for '5'.", () => {
      expect(toBase2Converter("5")).toStrictEqual("101");
   });

   test("Returns '-101' for '-5'.", () => {
      expect(toBase2Converter("-5")).toStrictEqual("-101");
   });


   test("Returns '-1010' for -10.", () => {
      expect(toBase2Converter(-10)).toStrictEqual("-1010");
   });

   test("Returns '1010' for 10.", () => {
      expect(toBase2Converter(10)).toStrictEqual("1010");
   });

   test("Returns '1010' for 10.2 ignoring the mantissa.", () => {
      expect(toBase2Converter(10.2)).toStrictEqual("1010");
   });

   test("Returns '1010' for 10.2 ignoring the mantissa.", () => {
      expect(toBase2Converter(-10.2)).toStrictEqual("-1010");
   });

   test("Illegal argument - invalid string.", () => {
      expect(() => toBase2Converter(Number("wefted"))).toThrow(TypeError);
      expect(() => toBase2Converter(Number("wefted"))).toThrow('Argument is not a number.');
   });
});

describe("substringOccurrencesCounter() test.", () => {
   test("Returns 0 for 'a' in 'test it'.", () => {
      expect(substringOccurrencesCounter('a', 'test it')).toStrictEqual(0);
   });

   test("Returns 3 for 't' in 'test it'.", () => {
      expect(substringOccurrencesCounter('t', 'test it')).toStrictEqual(3);
   });

   test("Returns 3 for 'T' in 'test it'.", () => {
      expect(substringOccurrencesCounter('t', 'test it')).toStrictEqual(3);
   });
});

describe("repeatingLitters() test.", () => {
   test("Returns 'HHeelloo' for 'Hello''.", () => {
      expect(repeatingLitters("Hello")).toStrictEqual("HHeellloo");
   });

   test("Returns 'HHeelloo' for 'Hello''.", () => {
      expect(repeatingLitters("Hello world")).toStrictEqual("HHeellloo  wworrldd");
   });
});

describe("redundant() test.", () => {
   test("Returns const f1() -> 'apple' for 'apple''.", () => {
      const f1 = redundant('apple');
      expect(f1()).toStrictEqual("apple");
   });

   test("Returns const f2() -> 'pear' for 'pear''.", () => {
      const f2 = redundant('pear');
      expect(f2()).toStrictEqual("pear");
   });

   test("Returns const f3() -> '' for '''.", () => {
      const f3 = redundant('');
      expect(f3()).toStrictEqual("");
   });
});

describe("matrixMultiplication() test.", () => {
   test("Returns [[2, 2], [2, 2]] for [[1, 1], [1, 1]] * [[1, 1], [1, 1]].", () => {
      const matrix1 = [[1, 1], [1, 1]];
      const matrix2 = [[1, 1], [1, 1]];
      expect(matrixMultiplication(matrix1, matrix2)).toStrictEqual([[2, 2], [2, 2]]);
   });

   test("Returns [[0]] for [[1, 0]] * [[0], [1]].", () => {
      const matrix1 = [[1, 0]];
      const matrix2 = [[0], [1]];
      expect(matrixMultiplication(matrix1, matrix2)).toStrictEqual([[0]]);
   });

   test("Returns [[1, 0], [0, 1]] * [[1, 0], [0, 1]].", () => {
      const identity = [[1, 0], [0, 1]];
      expect(matrixMultiplication(identity, identity)).toStrictEqual(identity);
   });

   test("Returns [[0, 0], [1, 0]] for [[0], [1]] * [[1, 0]].", () => {
      const matrix1 = [[0], [1]];
      const matrix2 = [[1, 0]];
      expect(matrixMultiplication(matrix1, matrix2)).toStrictEqual([[0, 0], [1, 0]]);
   });

   test("Throws error for [[0]] * [[1], [0]].", () => {
      const matrix1 = [[0]];
      const matrix2 = [[1], [0]];
      expect(() => matrixMultiplication(matrix1, matrix2)).toThrow(Error);
   });

});

describe("towerHanoi() test.", () => {
   test ("Returns 7 moves for 3 disks.", () => {
      expect(towerHanoi(3)).toStrictEqual([
         [0, 2], [0, 1], [2, 1], [0, 2], [1, 0], [1, 2], [0, 2]
      ]);
   });

   test ("Returns 31 moves for 5 disks.", () => {
      expect(towerHanoi(5)).toStrictEqual([
          [0, 2], [0, 1], [2, 1], [0, 2], [1, 0], [1, 2], [0, 2], [0, 1], [2, 1], [2, 0], [1, 0], [2, 1], [0, 2],
          [0, 1], [2, 1], [0, 2], [1, 0], [1, 2], [0, 2], [1, 0], [2, 1], [2, 0], [1, 0], [1, 2], [0, 2], [0, 1],
          [2, 1], [0, 2], [1, 0], [1, 2], [0, 2]
      ]);
   });
});

describe("gather() test.", () => {
   test ('Returns "abc" for gather("a")("b")("c").order(0)(1)(2).get().', () => {
      expect(gather("a")("b")("c").order(0)(1)(2).get()).toStrictEqual("abc");
   });

   test ('Returns "cba" for gather("a")("b")("c").order(2)(1)(0).get().', () => {
      expect(gather("a")("b")("c").order(2)(1)(0).get()).toStrictEqual("cba");
   });

   test ('Returns "hello!" for gather("e")("l")("o")("l")("!")("h").order(5)(0)(1)(3)(2)(4).get(.', () => {
      expect(gather("e")("l")("o")("l")("!")("h").order(5)(0)(1)(3)(2)(4).get()).toStrictEqual("hello!");
   });
});