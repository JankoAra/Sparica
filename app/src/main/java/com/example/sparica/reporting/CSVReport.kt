package com.example.sparica.reporting

import com.example.sparica.data.models.Spending

fun spendingsToCSV(spendings:List<Spending>):String{
    val sep="sep=,"
    val header =Spending.csvHeader()
    val data = mutableListOf<String>()
    for(s in spendings){
        data.add(s.asCsv())
    }
    return (listOf(sep,header) + data).joinToString("\n")
}

