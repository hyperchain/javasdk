use scale::{Decode, Encode};

use fvm_std::collections::hyper_map::HyperMap;
use fvm_std::runtime;
use macros::contract;
use macros::storage;

#[derive(Encode, Decode)]
pub struct Types {
    u81s: [u8; 1],
    u161s: [u16; 1],
    u321s: [u32; 1],
    u641s: [u64; 1],
    u1281s: [u128; 1],
    i81s: [i8; 1],
    i161s: [i16; 1],
    i321s: [i32; 1],
    i641s: [i64; 1],
    i1281s: [i128; 1],
    bool1s: [bool; 1],

    u812s: [[u8; 1]; 1],
    u1612s: [[u16; 1]; 1],
    u3212s: [[u32; 1]; 1],
    u6412s: [[u64; 1]; 1],
    u12812s: [[u128; 1]; 1],
    i812s: [[i8; 1]; 1],
    i1612s: [[i16; 1]; 1],
    i3212s: [[i32; 1]; 1],
    i6412s: [[i64; 1]; 1],
    i12812s: [[i128; 1]; 1],
    bool12s: [[bool; 1]; 1],
}

#[storage]
pub struct MyContract {

}

#[contract]
impl MyContract {
    fn new() -> Self {
        Self {

        }
    }

    pub fn make_types(&mut self, input: Types) -> Types {
        Types {
            u81s: [input.u81s[0] + 2],
            u161s: [2],
            u321s: [2],
            u641s: [2],
            u1281s: [2],
            i81s: [2],
            i161s: [2],
            i321s: [2],
            i641s: [2],
            i1281s: [2],
            bool1s: [false],
            u812s: [[1]],
            u1612s: [[1]],
            u3212s: [[1]],
            u6412s: [[1]],
            u12812s: [[1]],
            i812s: [[1]],
            i1612s: [[1]],
            i3212s: [[1]],
            i6412s: [[1]],
            i12812s: [[1]],
            bool12s: [[true]],
        }
    }
}