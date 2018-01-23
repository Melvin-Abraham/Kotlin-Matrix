import java.util.Random

class MatrixError(msg: String) : Throwable(msg)

data class num(val Data : Any? = 0) {

  var value = Data

  init {
    if (Data is num) { value = Data.value }
  }

  fun isInt() : Boolean {
    return value is Int || value is Long || value is Short
  }

  fun isDeci() : Boolean {
    return value is Float || value is Double
  }

  fun rational(deci: String) :  String {
    val deciStr = deci.toFloat().toString()
    val pow10 = deciStr.split(".")[1].length
    var denominator = power10(pow10)
    var numerator = (deciStr.toDouble() * denominator).toLong()
    val gcd = this.gcd(numerator, denominator)

    numerator = numerator / gcd
    denominator = denominator / gcd

    return "${numerator}/${denominator}"
  }

  private fun gcd(num1: Long, num2: Long) : Long {
    /* SOURCE:- https://www.programiz.com/kotlin-programming/examples/hcf-gcd */

    // Always set to positive
    var n1 = if (num1 > 0) num1 else -num1
    var n2 = if (num2 > 0) num2 else -num2

    while (n1 != n2) {
        if (n1 > n2)
            n1 -= n2
        else
            n2 -= n1
    }

    return n1
  }

  fun numType() : String {
    val type = listOf("Int", "Long", "Short", "Float", "Double")
    var dataType = ""

    for (a in type) {
      try {
        if (a == "Int" && this.value is Int) dataType = a
        else if (a == "Long" && this.value is Long) dataType = a
        else if (a == "Short" && this.value is Short) dataType = a
        else if (a == "Float" && this.value is Float) dataType = a
        else if (a == "Double" && this.value is Double) dataType = a
      }

      catch (e: java.lang.ClassCastException) {
        // Do Nothing
      }
    }
    return dataType
  }

  /* fun getVal() : Any {
    if (this.value is num) {
      return this.getValue()
    }
    else {
      return this.value
    }
  } */

  operator fun plus(other: num) : num {
    return num(this.toDouble() + other.toDouble())
  }

  operator fun minus(other: num) : num {
    return num(this.toDouble() - other.toDouble())
  }

  operator fun times(other: num) : num {
    return num(this.toDouble() * other.toDouble())
  }

  operator fun div(other: num) : num {
    return num(this.toDouble() / other.toDouble())
  }

  operator fun rem(other: num) : num {
    return num(this.toDouble() % other.toDouble())
  }

  fun toInt() : Int {
    return (this.value as Double).toInt()
  }

  fun toLong() : Long {
    return (this.value as Double).toLong()
  }

  fun toShort() : Short {
    return (this.value as Double).toShort()
  }

  fun toFloat() : Float {
    return (this.value.toString()).toFloat()
  }

  fun toDouble() : Double {
    return (this.value.toString()).toDouble()
  }

  override fun toString() : String {
    if (this.isInt()) {
      return this.value.toString()
    }

    else {
      if (this.toDouble() - this.toInt().toDouble() == 0.0) {
        return this.toInt().toString()
      }
      else {
        /* return this.rational(this.value.toString()) */
        return this.value.toString()
      }
    }
  }

  override operator fun equals(other: Any?) : Boolean {
    return this.toString() == num(other).toString()
  }

}

/* Extention Functions */

fun Int.toNum() : num {
  return num(this)
}

fun Long.toNum() : num {
  return num(this)
}

fun Float.toNum() : num {
  return num(this)
}

fun Double.toNum() : num {
  return num(this)
}

operator fun Int.plus(other: num) : num {
  return num(this) + other
}

operator fun Int.minus(other: num) : num {
  return num(this) - other
}

operator fun Int.times(other: num) : num {
  return num(this) * other
}

operator fun Int.div(other: num) : num {
  return num(this) / other
}

operator fun Int.rem(other: num) : num {
  return num(this) % other
}

fun MutableList<MutableList<Any>>.toNumList() : MutableList<MutableList<num>> {
  var mat = mutableListOf<MutableList<num>>()
  var list = mutableListOf<num>()

  for (a in 0..this.size - 1) {
    for (b in 0..this[a].size - 1) {
      list.add(num(this[a][b]))
    }
    mat.add(list)
    list = mutableListOf<num>()
  }

  return mat
}


data class matrix(var initMat: MutableList<MutableList<Any>> = mutableListOf(mutableListOf<Any>())) {

  var main = initMat.toNumList()

  companion object{
    fun create(cols : Int, elements: List<Any>) : matrix {
      var newMatrix = mutableListOf<MutableList<Any>>()
      var newList = mutableListOf<Any>()

      for (elem in 0..elements.size - 1) {
        newList.add(num(elements[elem]))

        if ((elem + 1) % cols == 0) {
          newMatrix.add(newList)
          newList = mutableListOf<Any>()
        }
      }

      if (newList.size != 0) {
        throw MatrixError("The following elements can't be allocated in the matrix: ${newList}")
      }

      return matrix(newMatrix)
    }

    fun identity(order: Int) : matrix {
      if (order < 1) {
        throw MatrixError("Can't create identity matrix of order ${order}")
      }

      var newMatrix = mutableListOf<MutableList<Any>>()
      var newList = mutableListOf<Any>()

      for (row in 0..order - 1) {
        for (col in 0..order - 1) {
          if (row == col) {
            newList.add(1)
          }

          else {
            newList.add(0)
          }
        }

        newMatrix.add(newList)
        newList = mutableListOf<Any>()
      }

      return matrix(newMatrix)
    }

    fun nullMatrix(rows: Int, cols: Int) : matrix {
      var list = listOf<Int>()

      for (a in 1..rows * cols) {
        list += listOf(0)
      }

      return matrix.create(cols, list)
    }

    fun random(rows: Int, cols: Int, min: Int = -100, max: Int = 100) : matrix {    // Inverse Error
      var newMatrix = mutableListOf<MutableList<Any>>()
      var newList = mutableListOf<Any>()

      for (row in 0..rows - 1) {
        for (col in 0..cols - 1) {
          newList.add(randInt(min, max).toInt())
        }
        newMatrix.add(newList)
        newList = mutableListOf<Any>()
      }

      return matrix(newMatrix)
    }
  }

  fun order() : List<Int> {
    val rows = this.main.size
    val cols = this.main[0].size

    return listOf(rows, cols)
  }

  fun isSqMatrix() : Boolean {
    return this.order()[0] == this.order()[1]
  }

  fun isSymmMatrix() : Boolean {
    return this == this.transpose()
  }

  fun isSkewSymmMatrix() : Boolean {
    return this == this.transpose() * (-1)
  }

  fun getElem(rowIndex: Int, colIndex: Int) : num {
    return num(this.main[rowIndex][colIndex])
  }

  fun setElem(rowIndex: Int, colIndex: Int, elem: Any, commit: Boolean = false) : matrix {
    var newMatrix = this.deepcopy().main
    newMatrix[rowIndex][colIndex] = num(elem)

    if (commit) this.main = newMatrix

    return matrix(newMatrix as MutableList<MutableList<Any>>)  // Check
  }

  fun getRow(rowIndex: Int) : matrix {
    return matrix(mutableListOf(this.main[rowIndex]) as MutableList<MutableList<Any>>)
  }

  fun getCol(colIndex: Int) : matrix {
    var list = mutableListOf<Any>()

    for (row in this.main) {
      list.add(row[colIndex])
    }

    return matrix(mutableListOf(list))
  }

  fun transpose() : matrix {
    var newMatrix = mutableListOf<MutableList<Any>>()

    for (a in 0..this.order()[1] - 1) {
      newMatrix.add(this.getCol(a).toMutableList())
    }

    return matrix(newMatrix)
  }

  fun flipVertically() : matrix {
    return matrix(this.main.reversed().toMutableList() as MutableList<MutableList<Any>>)
  }

  fun flipHorizontally() : matrix {
    return this.transpose().flipVertically().transpose()
  }

  fun rotateClock() : matrix {    // Not reqd.
    return this.flipVertically().transpose()
  }

  fun rotateAntiClock() : matrix {  // Not reqd.
    return this.transpose().flipVertically()
  }

  fun countElem() : Int {
    return this.toList().size
  }

  fun flatten() : matrix {
    var list = mutableListOf<num>()

    for (row in this.main) {
      for (elem in row) {
        list.add(elem)
      }
    }

    return matrix(mutableListOf(list) as MutableList<MutableList<Any>>)
  }

  fun reshape(cols: Int) : matrix {
    val list = this.toList()

    if (list.size % cols == 0) {
      return matrix.create(cols, list)
    }

    else {
      throw MatrixError("Can't reshape matrix of order ${this.order()} into a matrix with ${cols} col(s)")
    }
  }

  fun invertMatrix() : matrix {
    return this.reshape(this.order()[0])
  }

  fun minor(rowIndex: Int, colIndex: Int) : matrix {
    var newMatrix = this.deepcopy().main

    newMatrix.removeAt(rowIndex)

    for (row in 0..newMatrix.size - 1) {
      newMatrix[row].removeAt(colIndex)
    }

    return matrix(newMatrix as MutableList<MutableList<Any>>)
  }

  fun det() : num {
    if (! this.isSqMatrix()) {
      throw MatrixError("Can't find determinant of non-square matrix!")
    }

    else {
      var result = num(0)

      if (this.order() == listOf(1, 1)) {
        return this.getElem(0, 0)
      }

      else {
        for (col in 0..this.order()[1] - 1) {
          val Minor = this.minor(0, col)
          /* val expr = "${power(-1, col + 1)} * ${this[0, col]} * ${Minor.det()}" */
          result += powerMinus1(col) * this[0, col] * Minor.det()   // power is not def.
        }
      }

      return result
    }
  }

  fun adj() : matrix {
    if (! this.isSqMatrix()) {
      throw MatrixError("Can't find adjoint of a non-square matrix!")
    }

    else {
      val order = this.order()
      var newMatrix = matrix.nullMatrix(order[0], order[1])

      for (row in 0..order[0] - 1) {
        for (col in 0..order[1] - 1) {
          /* val expr = "newMatrix[$row, $col] = ${powerMinus1(row - col)} * ${this.minor(row, col).det()}" */
          newMatrix[row, col] = powerMinus1(row - col) * this.minor(row, col).det()
        }
      }

      return newMatrix.transpose()
    }
  }

  fun inverse() : matrix {
    if (! this.isSqMatrix()) {
      throw MatrixError("Can't find inverse of a non-square matrix!")
    }

    else {
      val det = this.det()

      if (det != num(0)) {
        return this.adj() * (1 / det)
      }
      else {
        throw MatrixError("Can't find inverse of a non-invertible matrix!")
      }

    }
  }

  fun toList() : List<Any> {
    return this.flatten().main[0].toList()
  }

  fun toMutableList() : MutableList<Any> {
    return this.flatten().main[0] as MutableList<Any>
  }

  override fun toString() : String {
    var s = ""
    var lenList = mutableListOf<Int>()

    for (a in 0..this.order()[1]) lenList.add(0)

    for (row in 0..this.order()[0] - 1) {
      for (col in 0..this.order()[1] - 1) {
        if (this[row, col].toString().length > lenList[col]) {
          lenList[col] = this[row, col].toString().length
        }
      }
    }

    for (row in 0..this.order()[0] - 1) {
      s += "[ "

      for (col in 0..this.order()[1] - 1) {
        s += this[row, col].toString().padStart(lenList[col], padChar = ' ') + " "
      }

      s += "]\n"
    }

    return s.slice(0..s.length - 2)     // Might be an issue for empty matrices
  }

  operator infix fun plus(mat: matrix) : matrix {
    if (this.order() != mat.order()) {
      throw MatrixError("Can't add matrices of different orders: ${this.order()} & ${mat.order()}")
    }

    else {
      var newMatrix = mutableListOf<MutableList<Any>>()
      var newList = mutableListOf<Any>()

      for (row in 0..this.order()[0] - 1) {
        for (col in 0..this.order()[1] - 1) {
          newList.add(this.getElem(row, col) + mat.getElem(row, col))
        }
        newMatrix.add(newList)
        newList = mutableListOf<Any>()
      }

      return matrix(newMatrix)
    }
  }

  operator infix fun minus(mat: matrix) : matrix {
    if (this.order() != mat.order()) {
      throw MatrixError("Can't subtract matrices of different orders: ${this.order()} & ${mat.order()}")
    }

    else {
      var newMatrix = mutableListOf<MutableList<Any>>()
      var newList = mutableListOf<Any>()

      for (row in 0..this.order()[0] - 1) {
        for (col in 0..this.order()[1] - 1) {
          newList.add(this.getElem(row, col) - mat.getElem(row, col))
        }
        newMatrix.add(newList)
        newList = mutableListOf<Any>()
      }

      return matrix(newMatrix)
    }
  }

  operator infix fun times(number: Any) : matrix {    // Function Overloading reqd.
    var newMatrix = this.deepcopy().main
    val _number = num(number)

    for (row in 0..this.order()[0] - 1) {
      for (col in 0..this.order()[1] - 1) {
        newMatrix[row][col] *= _number
      }
    }

    return matrix(newMatrix as MutableList<MutableList<Any>>)
  }

  operator infix fun times(mat: matrix) : matrix {
    if (this.order()[1] != mat.order()[0]) {
      throw MatrixError("Can't multiply matrices of orders ${this.order()} & ${mat.order()}")
    }

    else {
      var newMatrix = mutableListOf<MutableList<Any>>()
      var newList = mutableListOf<Any>()
      var rowList = newList
      var colList = newList
      var sum = num(0)

      for (row in 0..this.order()[0] - 1) {
        for (col in 0..mat.order()[1] - 1) {
          rowList = this.getRow(row).toMutableList()
          colList = mat.getCol(col).toMutableList()

          for (a in 0..rowList.size - 1) {
            sum += num(rowList[a]) * num(colList[a])
          }

          newList.add(sum)
          sum = num(0)
        }
        newMatrix.add(newList)
        newList = mutableListOf<Any>()
      }

      return matrix(newMatrix)
    }
  }

  operator fun get(rowIndex: Int, colIndex: Int) : num {
    return getElem(rowIndex, colIndex)
  }

  operator fun set(rowIndex: Int, colIndex: Int, elem: Any) : Unit {
    this.main[rowIndex][colIndex] = num(elem)
  }

  fun deepcopy() : matrix {
    var newMatrix = mutableListOf<MutableList<Any>>()

    for (row in this.main) {
      newMatrix.add(row.toMutableList())
    }

    return matrix(newMatrix)
  }
}

fun powerMinus1(pow: Int) : num {
  if (pow % 2 == 0) {
    return num(1)
  }

  else {
    return num(-1)
  }
}

fun power10(pow: Int) : Long {
  return ("1" + "0".repeat(pow)).toLong()
}

fun randInt(from: Int, to: Int) : Int {
  return Random().nextInt(to - from + 1) + from
}

fun main(args: Array<String>) {
  /* val matA = matrix.create(3, 10..22.toList()) */
  /* val matB = matrix.create(10, (1..100).toList()) */
  /* val matC = matrix.create(1, listOf(-2, 4, 5)) */
  /* val matD = matrix.create(3, listOf(1, 3, -6)) */
  /* val matE = matrix.create(3, (1..9).toList()) */
  /* val matF = matrix.create(3, listOf(1, 2, 4, -1, 3, 0, 4, 1, 0)) */
  /* val matG = matrix.create(2, listOf(2, 4, -1, 2)) */
  /* val matH = matrix.create(2, listOf(2, 3, 1, 4)) */
  /* val matI = matrix.create(3, listOf(1, 3, 3, 1, 4, 3, 1, 3, 4)) */
  /* val Iden = matrix.identity(3) */
  val Rand = matrix.random(3, 3, -500, 500)

  println(Rand)
  println("\n" + "-".repeat(40) + "\n")

  println(Rand.det())

  println("\n" + "-".repeat(40) + "\n")

  println(Rand.inverse())
}
