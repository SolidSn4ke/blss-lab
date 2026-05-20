package com.example.blsslab.model.doctype;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Item extends DocType {
    String item_code;
    String item_name;
    String item_group;
    String stock_uom;
}
