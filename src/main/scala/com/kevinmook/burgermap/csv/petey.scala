package com.kevinmook.burgermap.csv

object Main {
  def main(args: Array[String]): Unit = {
    val file = io.Source.fromFile("src/main/resources/burgerplaces.csv")
    
    val nameLookup = Map(
      3 -> "Ballin' Burgers",
      5 -> "Fantastic Fries",
      6 -> "There's Beer Here",
      7 -> "Grass Fed / Organic Available",
      8 -> "Vegetarian Friendly Fare"
    )
    
    val regex = "([^,]+),([^,]+)".r
    val m =
      file.getLines().foldLeft(Map[Int, Set[Int]]())((acc, line) => {
        line match {
          case regex(keyS, valueS) => {
            val key = keyS.toInt
            val value = valueS.toInt
            
            acc.get(key) match {
              case Some(existing) => acc + (key -> (existing + value))
              case None => acc + (key -> Set(value))
            }
          }
          case _ => {
            println("No match: '"+line+"'")
            acc
          }
        }
      })
    
    val sortedTuples = m.toList.sortWith(_._1 < _._1)
    
    sortedTuples.foreach{case (key, values) =>
      val valuesStrings: Set[String] = values.map(nameLookup.get(_).get)
      val csvValue = "\""+valuesStrings.reduce(_+","+_)+"\""
      println(key+","+csvValue)
    }
  }
}