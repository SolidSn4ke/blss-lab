module Booking exposing (..)

import Char exposing (toLower)
import Json.Encode as Encode


type alias Booking =
    { checkIn : String
    , checkOut : String
    , adultsCount : Int
    , childCount : Int
    , infantsCount : Int
    , petsCount : Int
    }


empty : Booking
empty =
    { checkIn = "", checkOut = "", adultsCount = 1, childCount = 0, infantsCount = 0, petsCount = 0 }


updateBooking : String -> String -> Booking -> Booking
updateBooking f v old =
    case String.toLower f of
        "checkin" ->
            { old | checkIn = v }

        "checkout" ->
            { old | checkOut = v }

        "adultscount" ->
            { old
                | adultsCount =
                    case String.toInt v of
                        Just x ->
                            x

                        _ ->
                            1
            }

        "childcount" ->
            { old
                | childCount =
                    case String.toInt v of
                        Just x ->
                            x

                        Nothing ->
                            0
            }

        "infantscount" ->
            { old
                | infantsCount =
                    case String.toInt v of
                        Just x ->
                            x

                        Nothing ->
                            0
            }

        "petscount" ->
            { old
                | petsCount =
                    case String.toInt v of
                        Just x ->
                            x

                        Nothing ->
                            0
            }

        _ ->
            Debug.todo "branch '_' not implemented"


encodeBooking : Booking -> Encode.Value
encodeBooking b =
    Encode.object
        [ ( "checkIn", Encode.string b.checkIn ),
        ( "checkOut", Encode.string b.checkOut ),
        ( "adultsCount", Encode.int b.adultsCount ),
        ( "childCount", Encode.int b.childCount ),
        ( "infantsCount", Encode.int b.infantsCount ),
        ( "petCount", Encode.int b.petsCount )
        ]
