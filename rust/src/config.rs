use crate::{Error, Result};

pub struct Config {
    pub dsn: String,
    pub token: String,
    pub service_name: Option<String>,
    pub environment: String,
}

impl Config {
    pub fn validate(&self) -> Result<()> {
        if self.dsn.is_empty() {
            return Err(Error::Config("DSN is required".into()));
        }
        if self.token.is_empty() {
            return Err(Error::Config("Token is required".into()));
        }
        Ok(())
    }
}
