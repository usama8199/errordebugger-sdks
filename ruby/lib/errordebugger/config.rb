module ErrorDebugger
  class Config
    attr_accessor :dsn, :token, :service_name, :environment

    def initialize(dsn:, token:, service_name: 'ruby-app', environment: 'production')
      raise ArgumentError, 'DSN is required' if dsn.nil? || dsn.empty?
      raise ArgumentError, 'Token is required' if token.nil? || token.empty?

      @dsn = dsn
      @token = token
      @service_name = service_name
      @environment = environment
    end
  end
end
