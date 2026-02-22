module Housing exposing (..)


type alias Housing =
    { price : Int, hType : HousingType }


type HousingType
    = Room
    | House
    | Hotel
    | Appartment
    | Hostel


show : HousingType -> String
show t =
    case t of
        Room ->
            "Комната"

        House ->
            "Дом"

        Hotel ->
            "Отель"

        Appartment ->
            "Квартира"

        Hostel ->
            "Хостел"
