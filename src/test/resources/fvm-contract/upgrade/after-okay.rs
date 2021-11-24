use scale::{Decode, Encode};

use fvm_std::collections::hyper_map::HyperMap;
use fvm_std::runtime;
use macros::contract;
use macros::storage;

#[storage]
pub struct MyContract {
    name: String,
    sum: u64,
    things: HyperMap<String, u64>,
    sum1: u64
}

#[contract]
impl MyContract {
    fn new() -> Self {
        let mut things = HyperMap::new();
        things.insert("key001".to_string(), 5);

        Self {
            name: "name".to_string(),
            sum: 5,
            things: things,
            sum1: 0
        }
    }

    pub fn set(&mut self, input: u64) {
        self.things.insert("key001".to_string(), input);
    }

    pub fn set_new(&mut self, input: u64) {
        self.sum1 = input
    }

    pub fn get_new(&mut self) -> u64 {
        self.sum1
    }

    pub fn get(&mut self) -> u64 {
        self.sum
    }
}