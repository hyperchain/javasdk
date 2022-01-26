use scale::{Decode, Encode};

use fvm_std::collections::hyper_map::HyperMap;
use fvm_std::runtime;
use macros::contract;
use macros::storage;

#[storage]
pub struct MyContract {
    sum: u64,
    things: HyperMap<String, u64>
}

#[contract]
impl MyContract {
    fn new() -> Self {
        let mut things = HyperMap::new();
        things.insert("key001".to_string(), 5);

        Self {
            sum: 5,
            things: things
        }
    }

    pub fn set(&mut self, input: u64) {
        self.things.insert("key001".to_string(), input);

    }

    pub fn get(&mut self) -> u64 {
        self.sum
    }
}