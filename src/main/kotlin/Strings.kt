/*
 * Захэшруем строки из наборов, чтобы можно было быстро проверять их на равентсво.
 * Вместо самих строк на равенство будем сравнивать по четыре хэша, вычисленных по разным модулям (много
 * хэшей считать не будем, но использование двух хэшей сильно снизит вероятность коллизий по сравнению
 * с использованием одного).
 */
var mass1 = setOf('!', ':', ';', ',', '.', '-')
var mass2 = setOf(':', ';', ',', '-')
/*
 * Функция, считающая хэш для одной строки
 */
fun hashOneString(s1 : String, flag : Int) : Int{
    var res = ""
    s1.forEach { ch ->
        if(flag == 0 || flag == 2) {
            res += ch
        }
        else if(flag == 1)
        {
            var t : Char = ch
            if(t.isUpperCase()){
                t = t.lowercaseChar()
            }
            res += t
        }
        else{
            if(ch !in mass2){
                res += ch
            }
        }
    }
    return res.hashCode()
}
/*
 * Функция, подсчитывающая хэши для набора строк
 */
fun hashes(n : Int, s1 : List <String>, flag : Int) : IntArray{
    var hashOf = IntArray(n) {0}
    for(i in 0 until n){
        hashOf[i] = hashOneString(s1[i], flag)
    }
    return hashOf
}


/*
 * Нормируем строки по пробелам
 */
fun spaces(s : String) : String{
    var flag : Boolean = false
    var sList = mutableListOf<Char>()
    s.forEach{ ch ->
        if(ch != ' '){
            if(!flag && sList.size > 0 && ch !in mass1){
                sList.add(' ')
            }
            else if(flag && sList.last() in mass1){
                sList.add(' ')
            }
            flag = true
        }
        else{
            flag = false
        }
        if(flag){
            sList.add(ch)
        }
    }
    var sList1 = sList.dropWhile({it == ' '})
    return sList1.joinToString("")
}
