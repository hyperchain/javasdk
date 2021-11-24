use macros::contract;
use macros::storage;
use fvm_std::runtime;
use scale::{Decode, Encode};
use fvm_std::collections::hyper_map::HyperMap;

#[storage]
pub struct SetHash {
    map: HyperMap<String, String>,
}

#[contract]
impl SetHash {
    fn new() -> Self {
        Self { map: HyperMap::new() }
    }

    pub fn set_hash(&mut self, key: String, value: String) {
        self.map.insert(key, value);
    }

    pub fn get_hash(&mut self, key: String) -> &String {
        self.map.get(&key).unwrap();
    }
}

#[cfg(test)]
mod tests {
    #[test]
    fn it_works() {
        assert_eq!(2 + 2, 4);
    }
}